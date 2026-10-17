compileVersion() {
	echo "Compiling NMS_Common for Spigot $1..."
	./gradlew :commandapi-bukkit-nms-common:clean :commandapi-bukkit-nms-common:build -PnmsCommonVersion="$2" --quiet
}

compileVersion "1.20 and 1.20.1" "1.20.1"
compileVersion "1.20.2" "1.20.2"
compileVersion "1.20.3 and 1.20.4" "1.20.4"
compileVersion "1.20.5 and 1.20.6" "1.20.6"
compileVersion "1.21 and 1.21.1" "1.21.1"
compileVersion "1.21.2 and 1.21.3" "1.21.3"
compileVersion "1.21.4" "1.21.4"
compileVersion "1.21.5" "1.21.5"
compileVersion "1.21.6 and 1.21.7 and 1.21.8" "1.21.6"
compileVersion "1.21.9 and 1.21.10" "1.21.9"
compileVersion "1.21.11" "1.21.11"