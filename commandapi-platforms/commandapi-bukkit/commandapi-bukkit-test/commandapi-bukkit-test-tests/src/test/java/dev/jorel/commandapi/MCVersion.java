package dev.jorel.commandapi;

import com.github.zafarkhaja.semver.Version;

public enum MCVersion {

	V1_19_3(Version.valueOf("1.19.3")),
	V1_19_2(Version.valueOf("1.19.2")),
	V1_19_1(Version.valueOf("1.19.1")),
	V1_19(Version.valueOf("1.19.0")),
	V1_18_2(Version.valueOf("1.18.2")),
	V1_18_1(Version.valueOf("1.18.1")),
	V1_18(Version.valueOf("1.18.0")),
	V1_17_1(Version.valueOf("1.17.1")),
	V1_17(Version.valueOf("1.17.0")),
	V1_16_5(Version.valueOf("1.16.5")),
	V1_16_4(Version.valueOf("1.16.4")),
	V1_16_3(Version.valueOf("1.16.3")),
	V1_16_2(Version.valueOf("1.16.2")),
	V1_16_1(Version.valueOf("1.16.1")),
	V1_16(Version.valueOf("1.16.0"));

	private Version version;

	MCVersion(Version version) {
		this.version = version;
	}

	public boolean greaterThanOrEqualTo(MCVersion version) {
		return this.version.greaterThanOrEqualTo(version.version);
	}

	public boolean lessThanOrEqualTo(MCVersion version) {
		return this.version.lessThanOrEqualTo(version.version);
	}
}