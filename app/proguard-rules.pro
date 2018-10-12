# Mapbox Plugin testapp ProGuard rules.

# --- GMS ---
-keep public class com.google.android.gms.* { public *; }
-dontwarn com.sun.xml.internal.ws.spi.db.*
-dontwarn com.google.android.gms.**