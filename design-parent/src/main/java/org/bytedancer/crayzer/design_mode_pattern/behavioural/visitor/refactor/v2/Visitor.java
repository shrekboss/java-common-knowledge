package org.bytedancer.crayzer.design_mode_pattern.behavioural.visitor.refactor.v2;

public interface Visitor {
    void visit(PdfFile pdfFile);
    void visit(PPTFile pptFile);
    void visit(WordFile wordFile);
}
