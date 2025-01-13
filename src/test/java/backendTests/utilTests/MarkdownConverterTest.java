package backendTests.utilTests;

import com.spirit.application.util.MarkdownConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testklasse für den MarkdownConverter.
 * Prüft die Umwandlung von Markdown in HTML in verschiedenen Szenarien.
 */
class MarkdownConverterTest {

    private MarkdownConverter markdownConverter;

    @BeforeEach
    void setUp() {
        // Erstellt ein MarkdownConverter-Objekt
        markdownConverter = new MarkdownConverter();
    }

    @Test
    void testConvertToHtmlSingleString() {
        // Wandelt einen einzelnen Markdown-String in HTML um
        // Beispiel-Text enthält Überschrift und Fettschrift
        String markdown = "# Heading\nThis is a **bold** text.";
        // Das erwartete Ergebnis kann je nach Leerzeilen/Zeilenumbrüchen minimal variieren [1]
        String expectedHtml = "<h1>Heading</h1>\n<p>This is a <strong>bold</strong> text.</p>\n";

        String result = markdownConverter.convertToHtml(markdown);
        assertEquals(expectedHtml, result);
    }

    @Test
    void testConvertToHtmlList() {
        // Wandelt mehrere Markdown-Strings in einer Liste um
        // Der erste String hat eine Überschrift, der zweite Fettschrift, der dritte eine Aufzählung
        List<String> markdownList = Arrays.asList(
                "# Heading 1",
                "This is a **bold** text.",
                "* Item 1\n* Item 2"
        );
        // Beispiel für erwartete HTML-Strings, Flexmark kann Zeilenumbrüche leicht anders handhaben [1]
        List<String> expectedHtmlList = Arrays.asList(
                "<h1>Heading 1</h1>\n",
                "<p>This is a <strong>bold</strong> text.</p>\n",
                "<ul>\n<li>Item 1</li>\n<li>Item 2</li>\n</ul>\n"
        );

        List<String> resultList = markdownConverter.convertToHtml(markdownList);
        assertEquals(expectedHtmlList, resultList);
    }

    @Test
    void testConvertToHtmlEmptyString() {
        // Wandelt einen leeren String um
        String markdown = "";
        String expectedHtml = "";

        String result = markdownConverter.convertToHtml(markdown);
        assertEquals(expectedHtml, result);
    }

    @Test
    void testConvertToHtmlNullInput() {
        // Bei null-Eingabe wird erwartet, dass eine NullPointerException geworfen wird [1]
        assertThrows(NullPointerException.class, () -> {
            markdownConverter.convertToHtml((String) null);
        });
    }
}
