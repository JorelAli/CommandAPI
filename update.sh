#!/bin/sh
echo "Old version: "
read -r oldVer
echo "New version: "
read -r newVer

############################
# CommandAPI Documentation #
############################

# Maven
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" docssrc/src/setup_shading.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" docssrc/src/setup_dev.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" docssrc/src/setup_annotations.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" docssrc/src/kotlinintro.md

# Gradle
sed -i "s/dev\.jorel:commandapi-bukkit-shade:$oldVer/dev\.jorel:commandapi-shade:$newVer/" docssrc/src/setup_shading.md
sed -i "s/dev\.jorel:commandapi-bukkit-core:$oldVer/dev\.jorel:commandapi-core:$newVer/" docssrc/src/setup_dev.md
sed -i "s/dev\.jorel:commandapi-annotations:$oldVer/dev\.jorel:commandapi-annotations:$newVer/" docssrc/src/setup_annotations.md
sed -i "s/dev\.jorel:commandapi-bukkit-kotlin:$oldVer/dev\.jorel:commandapi-bukkit-kotlin:$newVer/" docssrc/src/kotlinintro.md
sed -i "s/dev\.jorel:commandapi-velocity-kotlin:$oldVer/dev\.jorel:commandapi-velocity-kotlin:$newVer/" docssrc/src/kotlinintro.md

# mdBook documentation
sed -i "s/$oldVer/$newVer/" docssrc/book.toml
sed -i "s/$oldVer/$newVer/" docs/latest.html

###########
# Doxygen #
###########

sed -i "s/PROJECT_NUMBER         = $oldVer/PROJECT_NUMBER         = $newVer/" Doxyfile

############
# Examples #
############

# Maven example projects
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/kotlindsl/README.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/kotlindsl/pom.xml

sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/maven/README.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/maven/pom.xml

sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/maven-annotations/README.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/maven-annotations/pom.xml

sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/maven-shaded/README.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/maven-shaded/pom.xml

sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/maven-shaded-annotations/README.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/maven-shaded-annotations/pom.xml

sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/velocity/maven/README.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/velocity/maven-shaded/README.md

# Why doesn't this have a README?
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/commandtrees/pom.xml

# Gradle example projects
sed -i "s/dev\.jorel:commandapi-bukkit-plugin:$oldVer/dev\.jorel:commandapi-bukkit-plugin:$newVer/" examples/bukkit/gradle-groovy/README.md
sed -i "s/dev\.jorel:commandapi-bukkit-plugin:$oldVer/dev\.jorel:commandapi-bukkit-plugin:$newVer/" examples/bukkit/gradle-groovy/build.gradle

sed -i "s/dev\.jorel:commandapi-bukkit-plugin:$oldVer/dev\.jorel:commandapi-bukkit-plugin:$newVer/" examples/bukkit/gradle-kotlin/README.md
sed -i "s/dev\.jorel:commandapi-bukkit-plugin:$oldVer/dev\.jorel:commandapi-bukkit-plugin:$newVer/" examples/bukkit/gradle-kotlin/build.gradle.kts

sed -i "s/dev\.jorel:commandapi-bukkit-core:$oldVer/dev\.jorel:commandapi-bukkit-core:$newVer/" examples/bukkit/kotlindsl-gradle/README.md
sed -i "s/dev\.jorel:commandapi-bukkit-core:$oldVer/dev\.jorel:commandapi-bukkit-core:$newVer/" examples/bukkit/kotlindsl-gradle/build.gradle.kts
sed -i "s/dev\.jorel:commandapi-bukkit-kotlin:$oldVer/dev\.jorel:commandapi-bukkit-kotlin:$newVer/" examples/bukkit/kotlindsl-gradle/README.md
sed -i "s/dev\.jorel:commandapi-bukkit-kotlin:$oldVer/dev\.jorel:commandapi-bukkit-kotlin:$newVer/" examples/bukkit/kotlindsl-gradle/build.gradle.kts

sed -i "s/dev\.jorel:commandapi-velocity-core:$oldVer/dev\.jorel:commandapi-velocity-core:$newVer/" examples/velocity/gradle-groovy/README.md
sed -i "s/dev\.jorel:commandapi-velocity-core:$oldVer/dev\.jorel:commandapi-velocity-core:$newVer/" examples/velocity/gradle-groovy/build.gradle

sed -i "s/dev\.jorel:commandapi-velocity-core:$oldVer/dev\.jorel:commandapi-velocity-core:$newVer/" examples/velocity/gradle-kotlin/README.md
sed -i "s/dev\.jorel:commandapi-velocity-core:$oldVer/dev\.jorel:commandapi-velocity-core:$newVer/" examples/velocity/gradle-kotlin/build.gradle.kts

##############################
# CommandAPI project pom.xml #
##############################

# Set version in pom.xml using Maven
mvn versions:set -DnewVersion=$newVer
mvn versions:commit

mvn versions:set -DnewVersion=$newVer -P Platform.Bukkit
mvn versions:commit -P Platform.Bukkit

mvn versions:set -DnewVersion=$newVer -P Platform.Velocity
mvn versions:commit -P Platform.Velocity
