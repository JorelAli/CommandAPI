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
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" docssrc/src/test_setup.md

# Gradle
sed -i "s/dev\.jorel:commandapi-bukkit-shade:$oldVer/dev\.jorel:commandapi-bukkit-shade:$newVer/" docssrc/src/setup_shading.md
sed -i "s/dev\.jorel:commandapi-bukkit-shade-mojang-mapped:$oldVer/dev\.jorel:commandapi-bukkit-shade-mojang-mapped:$newVer/" docssrc/src/setup_shading.md
sed -i "s/dev\.jorel:commandapi-bukkit-core:$oldVer/dev\.jorel:commandapi-bukkit-core:$newVer/" docssrc/src/setup_dev.md
sed -i "s/dev\.jorel:commandapi-annotations:$oldVer/dev\.jorel:commandapi-annotations:$newVer/" docssrc/src/setup_annotations.md
sed -i "s/dev\.jorel:commandapi-kotlin-bukkit:$oldVer/dev\.jorel:commandapi-kotlin-bukkit:$newVer/" docssrc/src/kotlinintro.md
sed -i "s/dev\.jorel:commandapi-kotlin-velocity:$oldVer/dev\.jorel:commandapi-kotlin-velocity:$newVer/" docssrc/src/kotlinintro.md
sed -i "s/dev\.jorel:commandapi-bukkit-test-toolkit:$oldVer/dev\.jorel:commandapi-bukkit-test-toolkit:$newVer/" docssrc/src/test_setup.md
sed -i "s/dev\.jorel:commandapi-bukkit-core:$oldVer/dev\.jorel:commandapi-bukkit-core:$newVer/" docssrc/src/test_setup.md

# mdBook documentation
sed -i "s/$oldVer/$newVer/" docssrc/book.toml
sed -i "s/$oldVer/$newVer/" docs/latest.html

###########
# Doxygen #
###########

sed -i "s/PROJECT_NUMBER         = $oldVer/PROJECT_NUMBER         = $newVer/" Doxyfile

##########################
# Maven example projects #
##########################

##########
# Bukkit #
##########

sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/automated-tests/README.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/automated-tests/pom.xml

sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/automated-tests-shaded/README.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/automated-tests-shaded/pom.xml

# commandtrees README dose not reference a dependency version
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/commandtrees/pom.xml

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

# maven-shaded-tests README dose not reference a dependency version
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/bukkit/maven-shaded-tests/pom.xml

############
# Velocity #
############

sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/velocity/maven/README.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/velocity/maven/pom.xml

sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/velocity/maven-shaded/README.md
sed -i "s/<version>$oldVer<\/version>/<version>$newVer<\/version>/" examples/velocity/maven-shaded/pom.xml

###########################
# Gradle example projects #
###########################

#########
# Paper #
#########

# We're using Paper as the example dependency for example projects
sed -i "s/dev\.jorel:commandapi-paper-plugin:$oldVer/dev\.jorel:commandapi-paper-plugin:$newVer/" examples/bukkit/gradle-groovy/README.md
sed -i "s/dev\.jorel:commandapi-paper-plugin:$oldVer/dev\.jorel:commandapi-paper-plugin:$newVer/" examples/bukkit/gradle-groovy/build.gradle

sed -i "s/dev\.jorel:commandapi-paper-plugin:$oldVer/dev\.jorel:commandapi-paper-plugin:$newVer/" examples/bukkit/gradle-kotlin/README.md
sed -i "s/dev\.jorel:commandapi-paper-plugin:$oldVer/dev\.jorel:commandapi-paper-plugin:$newVer/" examples/bukkit/gradle-kotlin/build.gradle.kts

sed -i "s/dev\.jorel:commandapi-paper-core:$oldVer/dev\.jorel:commandapi-paper-core:$newVer/" examples/bukkit/kotlindsl-gradle/README.md
sed -i "s/dev\.jorel:commandapi-paper-core:$oldVer/dev\.jorel:commandapi-paper-core:$newVer/" examples/bukkit/kotlindsl-gradle/build.gradle.kts
sed -i "s/dev\.jorel:commandapi-kotlin-paper:$oldVer/dev\.jorel:commandapi-kotlin-paper:$newVer/" examples/bukkit/kotlindsl-gradle/README.md
sed -i "s/dev\.jorel:commandapi-kotlin-paper:$oldVer/dev\.jorel:commandapi-kotlin-paper:$newVer/" examples/bukkit/kotlindsl-gradle/build.gradle.kts

############
# Velocity #
############

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

#######################
# Manual update notes #
#######################

echo "IMPORTANT: Manual update notes"
echo "  ./docssrc/src/velocity_intro.html#adding-the-dependency: \`commandapi-velocity-shade\` dependencies should point to the latest SNAPSHOT version"
echo "  ./examples/bukkit/automated-tests/README.md: link to \`test_intro.html\` should point to the latest documentation version"
echo "  ./examples/bukkit/automated-tests-shaded/README.md: link to \`test_intro.html\` should point to the latest documentation version"