#/bin/sh
echo "Old version: "
read oldVer
echo "New version: "
read newVer
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" CommandAPI/commandapi-1.13/pom.xml
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" CommandAPI/commandapi-1.13.1/pom.xml
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" CommandAPI/commandapi-1.13.2/pom.xml
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" CommandAPI/commandapi-1.14/pom.xml
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" CommandAPI/commandapi-1.14.3/pom.xml
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" CommandAPI/commandapi-1.14.4/pom.xml
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" CommandAPI/commandapi-1.15/pom.xml
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" CommandAPI/commandapi-1.16.1/pom.xml
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" CommandAPI/commandapi-1.16.2/pom.xml

sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" CommandAPI/commandapi-core/pom.xml
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" CommandAPI/commandapi-plugin/pom.xml
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" CommandAPI/commandapi-shade/pom.xml
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" CommandAPI/commandapi-vh/pom.xml
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" CommandAPI/pom.xml

sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" docssrc/src/shading.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" docssrc/src/quickstart.md

sed -i "s/dev\.jorel:commandapi-shade:$oldVer/dev\.jorel:commandapi-shade:$newVer/" docssrc/src/shading.md
sed -i "s/dev\.jorel:commandapi-core:$oldVer/dev\.jorel:commandapi-core:$newVer/" docssrc/src/quickstart.md

sed -i "s/version: $oldVer/version: $newVer/" CommandAPI/commandapi-plugin/src/main/resources/plugin.yml

sed -i "s/PROJECT_NUMBER         = $oldVer/PROJECT_NUMBER         = $newVer/" Doxyfile
sed -i "s/$oldVer/$newVer/" docssrc/book.toml