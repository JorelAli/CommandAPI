module.exports = {
	"rules": {
		// Note: you must disable the base rule as it can report incorrect errors
		// note you must disable the base rule as it can report incorrect errors
		"no-unused-vars": "off",
		"@typescript-eslint/no-unused-vars": [
			"warn", // or "error"
			{
				"argsIgnorePattern": "^_",
				"varsIgnorePattern": "^_",
				"caughtErrorsIgnorePattern": "^_"
			}
		],
	}
};