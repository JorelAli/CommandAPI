hljs.registerLanguage("mccmd", function() {

    const COMMAND_NAME = {
        scope: 'title',
        match: /(?<=^\/)[a-zA-Z0-9]+/
    }

    const ENTITY_SELECTOR = {
        scope: 'string',
        match: /@[aeprs](\[.+\])?/
    };

    const NUMBER = {
        scope: 'number',
        match: /-?[0-9]+/
    };

    const TEMPLATE = {
        scope: 'comment',
        match: /<.*?>/
    };

    return {
        case_insensitive: true, // language is case-insensitive
        keywords: 'align anchored as at facing in positioned rotated run if store result score matches',
        contains: [
            COMMAND_NAME,
            ENTITY_SELECTOR,
            NUMBER,
            TEMPLATE
        ]
    };
});
hljs.highlightAll();