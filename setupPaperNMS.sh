echo "Setup Paper NMS for version 1.20.5..."
mvn --quiet paper-nms:init -pl :commandapi-paper-1.20.5 -P Platform.Paper

echo "Setup Paper NMS for version 1.21..."
mvn --quiet paper-nms:init -pl :commandapi-paper-1.21 -P Platform.Paper

echo "Done!"