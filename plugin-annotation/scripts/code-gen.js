#!/usr/bin/env node
'use strict';

const fs = require('fs');
const ejs = require('ejs');
const spec = require('./v8');
const _ = require('lodash');
const path = require('path');

global.iff = function (condition, val) {
  return condition() ? val : "";
};

global.camelize = function (str) {
  return str.replace(/(?:^|-)(.)/g, function (_, x) {
    return x.toUpperCase();
  });
};

global.camelizeWithLeadingLowercase = function (str) {
  return str.replace(/-(.)/g, function (_, x) {
    return x.toUpperCase();
  });
};

global.snakeCaseUpper = function snakeCaseUpper(str) {
  return str.replace(/-/g, "_").toUpperCase();
};

global.unhyphenate = function (str) {
 return str.replace(/-/g, " ");
};

global.geometryType = function (str) {
 if (str === "symbol" || str === "circle") {
   return "Point"
 } else if (str === "line") {
   return "LineString"
 } else if (str === "fill") {
   return "Polygon"
 } else {
   return "?"
 }
}

global.writeIfModified = function(filename, newContent) {
  try {
    const oldContent = fs.readFileSync(filename, 'utf8');
    if (oldContent == newContent) {
      console.warn(`* Skipping file '${filename}' because it is up-to-date`);
      return;
    }
  } catch(err) {
  }
  if (['0', 'false'].indexOf(process.env.DRY_RUN || '0') !== -1) {
    fs.writeFileSync(filename, newContent);
  }
  console.warn(`* Updating outdated file '${filename}'`);
};

// Specification parsing //
const lightProperties = Object.keys(spec[`light`]).reduce((memo, name) => {
  var property = spec[`light`][name];
  property.name = name;
  property['light-property'] = true;
  property.doc = property.doc.replace(/°/g,'&#xB0;');
  memo.push(property);
  return memo;
}, []);

// Collect layer types from spec
var layers = Object.keys(spec.layer.type.values).map((type) => {
  const layoutProperties = Object.keys(spec[`layout_${type}`]).reduce((memo, name) => {
    if (name !== 'visibility') {
      spec[`layout_${type}`][name].name = name;
      memo.push(spec[`layout_${type}`][name]);
    }
    return memo;
  }, []);

  const paintProperties = Object.keys(spec[`paint_${type}`]).reduce((memo, name) => {
    spec[`paint_${type}`][name].name = name;
    memo.push(spec[`paint_${type}`][name]);
    return memo;
  }, []);

  return {
    type: type,
    doc: spec.layer.type.values[type].doc,
    layoutProperties: layoutProperties,
    paintProperties: paintProperties,
    properties: layoutProperties.concat(paintProperties)
  };
});

// Process all layer properties
const layoutProperties = _(layers).map('layoutProperties').flatten().value();
const paintProperties = _(layers).map('paintProperties').flatten().value();
const allProperties = _(layoutProperties).union(paintProperties).union(lightProperties).value();
const enumProperties = _(allProperties).filter({'type': 'enum'}).value();

global.propertyType = function propertyType(property) {
  switch (property.type) {
      case 'boolean':
        return 'Boolean';
      case 'number':
        return 'Float';
      case 'formatted':
      case 'string':
        return 'String';
      case 'enum':
        return 'String';
      case 'color':
        return 'String';
      case 'array':
        return `${propertyType({type:property.value})}[]`;
      default:
        throw new Error(`unknown type for ${property.name}`);
  }
}

global.propertyJavaType = function propertyType(property) {
   switch (property.type) {
       case 'boolean':
         return 'boolean';
       case 'number':
         return 'float';
       case 'formatted':
       case 'string':
         return 'String';
       case 'enum':
         return 'String';
       case 'color':
         return 'String';
       case 'array':
         return `${propertyJavaType({type:property.value})}[]`;
       default:
         throw new Error(`unknown type for ${property.name}`);
   }
 }

global.propertyJNIType = function propertyJNIType(property) {
  switch (property.type) {
      case 'boolean':
        return 'jboolean';
      case 'number':
        return 'jfloat';
      case 'String':
        return 'String';
      case 'enum':
        return 'String';
      case 'color':
        return 'String';
      case 'array':
        return `jarray<${propertyType({type:property.value})}[]>`;
      default:
        return 'jobject*';
  }
}

global.propertyNativeType = function (property) {
  if (/-translate-anchor$/.test(property.name)) {
    return 'TranslateAnchorType';
  }
  if (/-(rotation|pitch|illumination)-alignment$/.test(property.name)) {
    return 'AlignmentType';
  }
  if (/^(text|icon)-anchor$/.test(property.name)) {
    return 'SymbolAnchorType';
  }
  switch (property.type) {
  case 'boolean':
    return 'bool';
  case 'number':
    return 'float';
  case 'formatted':
  case 'string':
    return 'std::string';
  case 'enum':
    if(property['light-property']){
       return `Light${camelize(property.name)}Type`;
    }
    return `${camelize(property.name)}Type`;
  case 'color':
    return `Color`;
  case 'array':
    if (property.length) {
      return `std::array<${propertyType({type: property.value})}, ${property.length}>`;
    } else {
      return `std::vector<${propertyType({type: property.value})}>`;
    }
  default: throw new Error(`unknown type for ${property.name}`)
  }
}

global.propertyTypeAnnotation = function propertyTypeAnnotation(property) {
  switch (property.type) {
      case 'enum':
        return `@Property.${snakeCaseUpper(property.name)}`;
      default:
        return "";
  }
};

global.defaultExpressionJava = function(property) {
    switch (property.type) {
      case 'boolean':
        return 'boolean';
      case 'number':
        return 'number';
      case 'formatted':
      case 'string':
        return "string";
      case 'enum':
        return "string";
      case 'color':
        return 'toColor';
      case 'array':
        return "array";
      default: return "string";
      }
}

global.defaultValueJava = function(property) {
    if(property.name.endsWith("-pattern")) {
        return '"pedestrian-polygon"';
    }
    if(property.name.endsWith("-font")) {
        return 'new String[]{"Open Sans Regular", "Arial Unicode MS Regular"}';
    }
     switch (property.type) {
      case 'boolean':
        return 'true';
      case 'number':
        return '0.3f';
      case 'formatted':
      case 'string':
        return '"' + property['default'] + '"';
      case 'enum':
        return snakeCaseUpper(property.name) + "_" + snakeCaseUpper(Object.keys(property.values)[0]);
      case 'color':
        return '"rgba(0, 0, 0, 1)"';
      case 'array':
             switch (property.value) {
              case 'formatted':
              case 'string':
                return '[' + property['default'] + "]";
              case 'number':
                var result ='new Float[] {';
                for (var i = 0; i < property.length; i++) {
                    result += "0f";
                    if (i +1 != property.length) {
                        result += ", ";
                    }
                }
                return result + "}";
             }
      default: throw new Error(`unknown type for ${property.name}`)
      }
}

/**
 * Produces documentation for property factory methods
 */
global.propertyFactoryMethodDoc = function (property) {
    var replaceIfPixels = function (doc) {
      return doc.replace('pixels', 'density-independent pixels')
    }
    let doc = replaceIfPixels(property.doc);
    // Match other items in back ticks
    doc = doc.replace(/`(.+?)`/g, function (m, symbol, offset, str) {
        if (str.substr(offset - 4, 3) !== 'CSS' && symbol[0].toUpperCase() != symbol[0] && _(enumProperties).filter({'name': symbol}).value().length > 0) {
            // Property 'enums'
            symbol = snakeCaseUpper(symbol);
            return '{@link Property.' + symbol + '}';
        } else if( _(allProperties).filter({'name': symbol}).value().length > 0) {
            // Other properties
            return '{@link PropertyFactory#' + camelizeWithLeadingLowercase(symbol) + '}';
        } else {
            // Left overs
            return '`' + symbol + '`';
        }
    });
    return doc;
};

/**
 * Produces documentation for property value constants
 */
global.propertyValueDoc = function (property, value) {

    // Match references to other property names & values.
    // Requires the format 'When `foo` is set to `bar`,'.
    let doc = property.values[value].doc.replace(/When `(.+?)` is set to `(.+?)`(?: or `([^`]+?)`)?,/g, function (m, peerPropertyName, propertyValue, secondPropertyValue, offset, str) {
        let otherProperty = snakeCaseUpper(peerPropertyName);
        let otherValue = snakeCaseUpper(peerPropertyName) + '_' + snakeCaseUpper(propertyValue);
        const firstPropertyValue = 'When {@link ' + `${otherProperty}` + '} is set to {@link Property#' + `${otherValue}` + '}';
        if (secondPropertyValue) {
            return firstPropertyValue + ` or {@link Property#${snakeCaseUpper(peerPropertyName) + '_' + snakeCaseUpper(secondPropertyValue)}},`;
        } else {
            return firstPropertyValue + ',';
        }
    });

    // Match references to our own property values.
    // Requires the format 'is equivalent to `bar`'.
    doc = doc.replace(/is equivalent to `(.+?)`/g, function(m, propertyValue, offset, str) {
        propertyValue = snakeCaseUpper(property.name) + '_' + snakeCaseUpper(propertyValue);
        return 'is equivalent to {@link Property#' + propertyValue + '}';
    });

    // Match other items in back ticks
    doc = doc.replace(/`(.+?)`/g, function (m, symbol, offset, str) {
        if ('values' in property && Object.keys(property.values).indexOf(symbol) !== -1) {
            // Property values
            propertyValue = snakeCaseUpper(property.name) + '_' + snakeCaseUpper(symbol);
            console.log("Transforming", symbol, propertyValue);
            return '{@link Property#' + `${propertyValue}` + '}';
        } else if (str.substr(offset - 4, 3) !== 'CSS' && symbol[0].toUpperCase() != symbol[0]) {
            // Property 'enums'
            symbol = snakeCaseUpper(symbol);
            return '{@link ' + symbol + '}';
        } else {
            // Left overs
            return symbol
        }
    });
    return doc;
};

global.isLightProperty = function (property) {
  return property['light-property'] === true;
};

global.propertyValueType = function (property) {
  switch (property['property-type']) {
    default:
      return `PropertyValue<${evaluatedType(property)}>`;
  }
};

global.evaluatedType = function (property) {
  if (/-translate-anchor$/.test(property.name)) {
    return 'TranslateAnchorType';
  }
  if (/-(rotation|pitch|illumination)-alignment$/.test(property.name)) {
    return 'AlignmentType';
  }
  if (/^(text|icon)-anchor$/.test(property.name)) {
    return 'SymbolAnchorType';
  }
  if (/position/.test(property.name)) {
    return 'Position';
  }
  switch (property.type) {
  case 'boolean':
    return 'bool';
  case 'number':
    return 'float';
  case 'formatted':
  case 'string':
    return 'std::string';
  case 'enum':
    return (isLightProperty(property) ? 'Light' : '') + `${camelize(property.name)}Type`;
  case 'color':
    return `Color`;
  case 'array':
    if (property.length) {
      return `std::array<${evaluatedType({type: property.value})}, ${property.length}>`;
    } else {
      return `std::vector<${evaluatedType({type: property.value})}>`;
    }
  default: throw new Error(`unknown type for ${property.name}`)
  }
};

global.supportsZoomFunction = function (property) {
  return property.expression && property.expression.parameters.indexOf('zoom') > -1;
};

global.supportsPropertyFunction = function (property) {
  return property['property-type'] === 'data-driven' || property['property-type'] === 'cross-faded-data-driven';
};

// Template processing //
// Java
const annotationJava = ejs.compile(fs.readFileSync('plugin-annotation/scripts/annotation.java.ejs', 'utf8'), {strict: true});
const annotationOptionsJava = ejs.compile(fs.readFileSync('plugin-annotation/scripts/annotation_options.java.ejs', 'utf8'), {strict: true});
const annotationManagerJava = ejs.compile(fs.readFileSync('plugin-annotation/scripts/annotation_manager.java.ejs', 'utf8'), {strict: true});
const annotationElementProvider = ejs.compile(fs.readFileSync('plugin-annotation/scripts/annotation_element_provider.java.ejs', 'utf8'), {strict: true});
const annotationDragListener =  ejs.compile(fs.readFileSync('plugin-annotation/scripts/annotation_drag_listener.java.ejs', 'utf8'), {strict: true});
const annotationClickListener =  ejs.compile(fs.readFileSync('plugin-annotation/scripts/annotation_click_listener.java.ejs', 'utf8'), {strict: true});
const annotationLongClickListener =  ejs.compile(fs.readFileSync('plugin-annotation/scripts/annotation_long_click_listener.java.ejs', 'utf8'), {strict: true});
const annotationJavaInstrumentationTests= ejs.compile(fs.readFileSync('plugin-annotation/scripts/annotation_instrumentation_test.junit.ejs', 'utf8'), {strict: true});
const annotationManagerJavaInstrumentationTests = ejs.compile(fs.readFileSync('plugin-annotation/scripts/annotation_manager_instrumentation_test.junit.ejs', 'utf8'), {strict: true});
const annotationManagerJavaUnitTests = ejs.compile(fs.readFileSync('plugin-annotation/scripts/annotation_manager_unit_test.junit.ejs', 'utf8'), {strict: true});

for (const layer of layers) {
  if(layer.type === "symbol" || layer.type === "circle" || layer.type === "fill" || layer.type === "line"){
      writeIfModified(`plugin-annotation/src/main/java/com/mapbox/mapboxsdk/plugins/annotation/On${camelize(layer.type)}DragListener.java`, annotationDragListener(layer));
      writeIfModified(`plugin-annotation/src/main/java/com/mapbox/mapboxsdk/plugins/annotation/On${camelize(layer.type)}ClickListener.java`, annotationClickListener(layer));
      writeIfModified(`plugin-annotation/src/main/java/com/mapbox/mapboxsdk/plugins/annotation/On${camelize(layer.type)}LongClickListener.java`, annotationLongClickListener(layer));
      writeIfModified(`plugin-annotation/src/main/java/com/mapbox/mapboxsdk/plugins/annotation/${camelize(layer.type)}.java`, annotationJava(layer));
      writeIfModified(`plugin-annotation/src/main/java/com/mapbox/mapboxsdk/plugins/annotation/${camelize(layer.type)}Options.java`, annotationOptionsJava(layer));
      writeIfModified(`plugin-annotation/src/main/java/com/mapbox/mapboxsdk/plugins/annotation/${camelize(layer.type)}Manager.java`, annotationManagerJava(layer));
      writeIfModified(`plugin-annotation/src/main/java/com/mapbox/mapboxsdk/plugins/annotation/${camelize(layer.type)}ElementProvider.java`, annotationElementProvider(layer));
      writeIfModified(`app/src/androidTest/java/com/mapbox/mapboxsdk/plugins/annotation/${camelize(layer.type)}Test.java`, annotationJavaInstrumentationTests(layer));
      writeIfModified(`app/src/androidTest/java/com/mapbox/mapboxsdk/plugins/annotation/${camelize(layer.type)}ManagerTest.java`, annotationManagerJavaInstrumentationTests(layer));
      writeIfModified(`plugin-annotation/src/test/java/com/mapbox/mapboxsdk/plugins/annotation/${camelize(layer.type)}ManagerTest.java`, annotationManagerJavaUnitTests(layer));
  }
}