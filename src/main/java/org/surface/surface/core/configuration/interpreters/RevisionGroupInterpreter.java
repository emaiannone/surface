package org.surface.surface.core.configuration.interpreters;

import org.surface.surface.core.engine.analysis.selectors.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class RevisionGroupInterpreter {
    private static final Map<String, Class<? extends RevisionSelector>> SUPPORTED_SELECTORS;
    static {
        // NOTE Any new selection type must be added here to be recognized by the CLI parser
        SUPPORTED_SELECTORS = new HashMap<>();
        SUPPORTED_SELECTORS.put(RangeRevisionSelector.CODE.toLowerCase(), RangeRevisionSelector.class);
        SUPPORTED_SELECTORS.put(FromRevisionSelector.CODE.toLowerCase(), FromRevisionSelector.class);
        SUPPORTED_SELECTORS.put(ToRevisionSelector.CODE.toLowerCase(), ToRevisionSelector.class);
        SUPPORTED_SELECTORS.put(SingleRevisionSelector.CODE.toLowerCase(), SingleRevisionSelector.class);
        SUPPORTED_SELECTORS.put(AllRevisionSelector.CODE.toLowerCase(), AllRevisionSelector.class);
    }

    public static RevisionSelector interpretRevisionGroup(String revisionMode, String revisionString) {
        RevisionSelector revisionSelector;
        try {
            Class<? extends RevisionSelector> aClass = SUPPORTED_SELECTORS.get(revisionMode);
            if (aClass == null) {
                return new HeadRevisionSelector(null);
            }
            Constructor<? extends RevisionSelector> constructor = aClass.getConstructor(String.class);
            revisionSelector = constructor.newInstance(revisionString);
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException |
                 NoSuchMethodException e) {
            throw new IllegalArgumentException("The revision string supplied is invalid.");
        }
        return revisionSelector;
    }
}
