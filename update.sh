#!/bin/sh
echo "Old version: "
read -r oldVer
echo "New version: "
read -r newVer

sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" docssrc/src/shading.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" docssrc/src/quickstart.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" docssrc/src/annotationsetup.md


sed -i "s/dev\.jorel:commandapi-shade:$oldVer/dev\.jorel:commandapi-shade:$newVer/" docssrc/src/shading.md
sed -i "s/dev\.jorel:commandapi-core:$oldVer/dev\.jorel:commandapi-core:$newVer/" docssrc/src/quickstart.md
sed -i "s/dev\.jorel:commandapi-annotations:$oldVer/dev\.jorel:commandapi-annotations:$newVer/" docssrc/src/annotationsetup.md

sed -i "s/version: $oldVer/version: $newVer/" CommandAPI/commandapi-plugin/src/main/resources/plugin.yml

sed -i "s/PROJECT_NUMBER         = $oldVer/PROJECT_NUMBER         = $newVer/" Doxyfile
sed -i "s/$oldVer/$newVer/" docssrc/book.toml

cd CommandAPI
mvn versions:set -DnewVersion=$newVer
mvn versions:commit
