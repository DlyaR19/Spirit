package com.spirit.application.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

/**
 * Component for converting Markdown text into HTML.
 */
@Component
public class MarkdownConverter {
    private final Parser parser; // Parser für Markdown
    private final HtmlRenderer renderer; // Renderer für HTML

    public MarkdownConverter() {
        this.parser = Parser.builder().build();
        this.renderer = HtmlRenderer.builder().build();
    }

    /**
     * Converts a single Markdown string into HTML.
     * @param markdown the Markdown string to convert
     * @return the converted HTML string
     */
    public String convertToHtml(String markdown) {
        return renderer.render(parser.parse(markdown));
    }

    /**
     * Converts a list of Markdown strings into a list of HTML strings.
     * @param markdownList the list of Markdown strings to convert
     * @return the list of converted HTML strings
     */
    public List<String> convertToHtml(List<String> markdownList) {
        List<String> htmlList = new ArrayList<>();
        for (String markdown : markdownList) {
            String html = renderer.render(parser.parse(markdown));
            htmlList.add(html);
        }
        return htmlList;
    }
}
