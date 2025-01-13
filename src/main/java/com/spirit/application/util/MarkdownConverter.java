package com.spirit.application.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

/**
 * Komponente zur Konvertierung von Markdown in HTML
 */

@Component
public class MarkdownConverter {
    private final Parser parser; // Parser für Markdown
    private final HtmlRenderer renderer; // Renderer für HTML

    /**
     * Konstruktor initialisiert Parser und Renderer
     */
    public MarkdownConverter() {
        this.parser = Parser.builder().build();
        this.renderer = HtmlRenderer.builder().build();
    }

    /**
     * Konvertiert einen einzelnen Markdown-String in HTML
     * @param markdown Der zu konvertierende Markdown-Text
     * @return Der resultierende HTML-String
     */
    public String convertToHtml(String markdown) {
        if (markdown == null) {
            throw new NullPointerException("Markdown input cannot be null");
        }
        return renderer.render(parser.parse(markdown));
    }

    /**
     * Konvertiert eine Liste von Markdown-Strings in HTML
     * @param markdownList Liste der zu konvertierenden Markdown-Texte
     * @return Liste der konvertierten HTML-Strings
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
