package com.kosa.kmt.nonController.markdown;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

@Service
public class MarkdownService {
    private static final Parser parser = Parser.builder().build();
    private static final HtmlRenderer renderer = HtmlRenderer.builder().build();

    public static String renderMarkdownToHtml(String markdown) {
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }}
