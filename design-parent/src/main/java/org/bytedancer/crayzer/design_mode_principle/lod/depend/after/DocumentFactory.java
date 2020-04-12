package org.bytedancer.crayzer.design_mode_principle.lod.depend.after;

import org.bytedancer.crayzer.design_mode_principle.lod.depend.Html;

public class DocumentFactory {

    private HtmlDownloader downloader;

    public DocumentFactory(HtmlDownloader downloader) {
        this.downloader = downloader;
    }

    public Document createDocument(String url) {
        Html html = downloader.downloadHtml(url);
        return new Document(url, html);
    }
}
