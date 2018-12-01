echo "Enter the version (For example, 1.7): "
read version
mvn install:install-file -DgroupId=io.github.jorelali -DartifactId=commandapi -Dversion=$version -Dfile=./CommandAPI.jar -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=.  -DcreateChecksum=true
