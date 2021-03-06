package org.zwobble.mammoth.internal.docx;

import org.zwobble.mammoth.internal.documents.NumberingLevel;

import java.util.Map;
import java.util.Optional;

import static org.zwobble.mammoth.internal.util.Maps.lookup;
import static org.zwobble.mammoth.internal.util.Maps.map;

public class Numbering {
    public static final Numbering EMPTY = new Numbering(map());

    private final Map<String, Map<String, NumberingLevel>> numbering;

    public Numbering(Map<String, Map<String, NumberingLevel>> numbering) {
        this.numbering = numbering;
    }

    public Optional<NumberingLevel> findLevel(String numId, String level) {
        return lookup(numbering, numId)
            .flatMap(levels -> lookup(levels, level));
    }
}
