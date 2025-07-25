package com.dtflys.forest.exceptions;

import com.dtflys.forest.mapping.ForestExpressionException;
import com.dtflys.forest.mapping.MappingTemplate;
import com.dtflys.forest.reflection.ForestMethod;

import java.lang.annotation.Annotation;

/**
 * Forest变量未定义异常
 *
 * @author gongjun[dt_flys@hotmail.com]
 * @since 1.5.0
 */
public class ForestIndexReferenceException extends ForestExpressionException {


    private final int index;
    
    private final int argumentsLength;


    public ForestIndexReferenceException(int index, int argumentsLength) {
        this(null, null, null, index, argumentsLength, null, -1, -1);
    }

    public ForestIndexReferenceException(Class<? extends Annotation> annotationType, String attributeName, ForestMethod method, int index, int argumentsLength, MappingTemplate source, int startIndex, int endIndex) {
        this(annotationType, attributeName, method, index, argumentsLength, source, startIndex, endIndex, null);
    }


    public ForestIndexReferenceException(Class<? extends Annotation> annotationType, String attributeName, ForestMethod method, int index, int argumentsLength, MappingTemplate source, int startIndex, int endIndex, Throwable cause) {
        super("Index " + index + " out of bounds for arguments length " + argumentsLength, annotationType, attributeName, method, source, startIndex, endIndex, cause);
        this.index = index;
        this.argumentsLength = argumentsLength;
    }


    public int getIndex() {
        return index;
    }

    public int getArgumentsLength() {
        return argumentsLength;
    }
}
