#!/bin/sh
echo "Old version: "
read -r oldVer
echo "New version: "
read -r newVer

# Maven
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" docssrc/src/shading.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" docssrc/src/quickstart.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" docssrc/src/annotationsetup.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" docssrc/src/kotlinintro.md

# Gradle
sed -i "s/dev\.jorel:commandapi-shade:$oldVer/dev\.jorel:commandapi-shade:$newVer/" docssrc/src/shading.md
sed -i "s/dev\.jorel:commandapi-core:$oldVer/dev\.jorel:commandapi-core:$newVer/" docssrc/src/quickstart.md
sed -i "s/dev\.jorel:commandapi-annotations:$oldVer/dev\.jorel:commandapi-annotations:$newVer/" docssrc/src/annotationsetup.md
sed -i "s/dev\.jorel:commandapi-kotlin:$oldVer/dev\.jorel:commandapi-kotlin:$newVer/" docssrc/src/kotlinintro.md

# Doxygen
sed -i "s/PROJECT_NUMBER         = $oldVer/PROJECT_NUMBER         = $newVer/" Doxyfile

# mdBook documentation
sed -i "s/$oldVer/$newVer/" docssrc/book.toml
sed -i "s/$oldVer/$newVer/" docs/latest.html

# Example projects
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/kotlindsl/README.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/maven/README.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/maven-annotations/README.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/maven-shaded/README.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/maven-shaded-annotations/README.md

sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/commandtrees/pom.xml
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/kotlindsl/pom.xml
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/maven/pom.xml
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/maven-annotations/pom.xml
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/maven-shaded/pom.xml
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/maven-shaded-annotations/pom.xml

# Set version in pom.xml using Maven
mvn versions:set -DnewVersion=$newVer
mvn versions:commit
