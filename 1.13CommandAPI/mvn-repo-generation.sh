echo "Enter the version (For example, 1.7): "
read version
perl -p -i -e "s|<version>dev</version>|<version>$version</version>|g" ./pom.xml
mvn install:install-file -DgroupId=io.github.jorelali -DartifactId=commandapi -Dversion=$version -Dfile=./CommandAPI.jar -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=.  -DcreateChecksum=true -DpomFile=./pom.xml
