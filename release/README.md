# Release script
This python package attempts to automate the majority of steps required when do a release. To be more specific, looking over the release checklist, the following items are now automated:

- Change version name to proper release versions in `gradle.properties`
- Kicks off release build in CI
- Generates Javadoc and opens PR in android-docs

## One time setup
execute `pip install blessings` in terminal

## Generate Javadoc
We assume your git projects are stored in a single folder like `development`. Assuming this means that the `android-doc` cloned repo needs to be `../../` from the `release` package.