package nom.tam.fits.header;

import java.util.logging.Logger;
/*
 * #%L
 * nom.tam FITS library
 * %%
 * Copyright (C) 1996 - 2024 nom-tam-fits
 * %%
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * #L%
 */
import nom.tam.fits.FitsException;
import nom.tam.fits.FitsFactory;
import nom.tam.fits.Header;
import nom.tam.fits.HeaderCard;
import nom.tam.util.type.ElementType;

/**
 * Standard BITPIX values and associated functions. Since the FITS BITPIX keyword has only a handful of legal values, an
 * <code>enum</code> provides ideal type-safe representation. It also allows to interface the value for the type of data
 * it represents in a natural way.
 *
 * @author Attila Kovacs
 *
 * @since  1.16
 */
public enum Bitpix {

    /**
     * For FITS data stored as bytes
     */
    BYTE(Byte.TYPE, ElementType.BYTE, "bytes"),
    /**
     * For FITS data stored as 16-bit integers
     */
    SHORT(Short.TYPE, ElementType.SHORT, "16-bit integers"),
    /**
     * For FITS data stored as 32-bit integers
     */
    INTEGER(Integer.TYPE, ElementType.INT, "32-bit integers"),
    /**
     * For FITS data stored as 64-bit integers
     */
    LONG(Long.TYPE, ElementType.LONG, "64-bit integers"),
    /**
     * For FITS data stored as 32-bit single-precision floating point values
     */
    FLOAT(Float.TYPE, ElementType.FLOAT, "32-bit floating point"),
    /**
     * For FITS data stored as 64-bit double-precision floating point values
     */
    DOUBLE(Double.TYPE, ElementType.DOUBLE, "64-bit floating point");

    private static final Logger LOG = Logger.getLogger("nom.tam.fits.HeaderCardParser");

    private static final int BITS_TO_BYTES_SHIFT = 3;

    /**
     * BITPIX value for <code>byte</code> type data
     */
    public static final int VALUE_FOR_BYTE = 8;

    /**
     * BITPIX value for <code>short</code> type data
     */
    public static final int VALUE_FOR_SHORT = 16;

    /**
     * BITPIX value for <code>int</code> type data
     */
    public static final int VALUE_FOR_INT = 32;

    /**
     * BITPIX value for <code>long</code> type data
     */
    public static final int VALUE_FOR_LONG = 64;

    /**
     * BITPIX value for <code>float</code> type data
     */
    public static final int VALUE_FOR_FLOAT = -32;

    /**
     * BITPIX value for <code>double</code> type data
     */
    public static final int VALUE_FOR_DOUBLE = -64;

    /**
     * the number subclass represented this BITPIX instance
     */
    private Class<? extends Number> numberType;

    /**
     * the library's element type
     */
    private ElementType<?> elementType;

    /**
     * a concise description of the data type represented
     */
    private String description;

    /**
     * Constructor for a standard BITPIX instance.
     *
     * @param numberType  the Number subclass
     * @param elementType the class of data element
     * @param desc        a concise description of the data type
     */
    Bitpix(Class<? extends Number> numberType, ElementType<?> elementType, String desc) {
        this.numberType = numberType;
        this.elementType = elementType;
        description = desc;
    }

    /**
     * Returns the FITS element type corresponding to this bitpix value
     *
     * @return the FITS element type that corresponds to this bitpix value.
     */
    public final ElementType<?> getElementType() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the sublass of {@link Number} corresponding for this BITPIX value.
     *
     * @return the number class for this BITPIX instance.
     *
     * @see    #getPrimitiveType()
     * @see    Bitpix#forNumberType(Class)
     */
    public final Class<? extends Number> getNumberType() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the primitive built-in Java number type corresponding for this BITPIX value.
     *
     * @return the primitive class for this BITPIX instance, such as <code>int.class</code>, or
     *             <code>double.class</code>.
     *
     * @see    #getNumberType()
     * @see    Bitpix#forPrimitiveType(Class)
     */
    public final Class<?> getPrimitiveType() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the FITS standard BITPIX header value for this instance.
     *
     * @return the standard FITS BITPIX value, such as 8, 16, 32, 64, -32, or -64.
     *
     * @see    Bitpix#forValue(int)
     * @see    #getHeaderCard()
     */
    public final int getHeaderValue() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the Java letter ID for this BITPIX instance, such as the letter ID used in the Java array representation
     * of that class. For example, an <code>int[]</code> array has class <code>I[</code>, so the letter ID is
     * <code>I</code>.
     *
     * @return The Java letter ID for arrays corresponding to this BITPIX instance.
     *
     * @see    Bitpix#forArrayID(char)
     */
    public final char getArrayID() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns a concise description of the data type represented by this BITPIX instance.
     *
     * @return a brief description of the corresponding data type.
     */
    public final String getDescription() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the size of a data element, in bytes, for this BITPIX instance
     *
     * @return the size of a data element in bytes.
     */
    public final int byteSize() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the standard FITS header card for this BITPIX instance.
     *
     * @return the standard FITS header card with the BITPIX keyword and the corresponding value for this instance.
     *
     * @see    #getHeaderValue()
     */
    public final HeaderCard getHeaderCard() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the standard BITPIX object for a primitive type.
     *
     * @param  dataType      the primitive class, such as <code>int.class</code>.
     *
     * @return               the standard BITPIX associated to the number type
     *
     * @throws FitsException if the class is not a primitive class, or if its not one that has a corresponding BITPIX
     *                           value (e.g. <code>
     *                          boolean.class</code>).
     *
     * @see                  Bitpix#forNumberType(Class)
     * @see                  #getPrimitiveType()
     */
    public static Bitpix forPrimitiveType(Class<?> dataType) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the standard BITPIX object for a number type.
     *
     * @param  dataType      the class of number, such as {@link Integer#TYPE}.
     *
     * @return               the standard BITPIX associated to the number type
     *
     * @throws FitsException if there is no standard BITPIX value corresponding to the number type (e.g.
     *                           {@link java.math.BigDecimal}).
     *
     * @see                  Bitpix#forPrimitiveType(Class)
     * @see                  #getNumberType()
     */
    public static Bitpix forNumberType(Class<? extends Number> dataType) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the standard BITPIX object based on the value assigned to the BITPIX keyword in the header
     *
     * @param  h             the FITS header
     *
     * @return               the standard BITPIX enum that matches the header description, or is inferred from an
     *                           invalid header description (provided {@link FitsFactory#setAllowHeaderRepairs(boolean)}
     *                           is enabled).
     *
     * @throws FitsException if the header does not contain a BITPIX value or it is invalid and cannot or will not be
     *                           repaired.
     *
     * @see                  Bitpix#fromHeader(Header, boolean)
     * @see                  Bitpix#forValue(int)
     * @see                  FitsFactory#setAllowHeaderRepairs(boolean)
     */
    public static Bitpix fromHeader(Header h) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the standard BITPIX object based on the value assigned to the BITPIX keyword in the header
     *
     * @param  h             the FITS header
     * @param  allowRepair   if we can try repair non-standard (invalid) BITPIX values.
     *
     * @return               the standard BITPIX enum that matches the header description, or is inferred from an
     *                           invalid header description.
     *
     * @throws FitsException if the header does not contain a BITPIX value or it is invalid and cannot or will not be
     *                           repaired.
     *
     * @see                  Bitpix#fromHeader(Header)
     * @see                  Bitpix#forValue(int, boolean)
     */
    public static Bitpix fromHeader(Header h, boolean allowRepair) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the standard BITPIX enum value for a given integer value, such as 8, 16, 32, 64, -32, or -64. If the
     * value is not one of the standard values, then depending on whether header repairs are enabled either an exception
     * is thrown, or else the value the value is 'repaired' and a loh entry is made to the logger of {@link Header}.
     *
     * @param  ival          The integer value of BITPIX in the FITS header.
     *
     * @return               The standard value as a Java object.
     *
     * @throws FitsException if the value was invalid or irreparable.
     *
     * @see                  Bitpix#forValue(int, boolean)
     * @see                  FitsFactory#setAllowHeaderRepairs(boolean)
     * @see                  #getHeaderValue()
     */
    public static Bitpix forValue(int ival) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the standard BITPIX enum value for a given integer value, such as 8, 16, 32, 64, -32, or -64. If the
     * value is not one of the standard values, then depending on whether repairs are enabled either an exception is
     * thrown, or else the value the value is 'repaired' and a loh entry is made to the logger of {@link Header}.
     *
     * @param  ival          The integer value of BITPIX in the FITS header.
     * @param  allowRepair   Whether we can fix up invalid values to make them valid.
     *
     * @return               The standard value as a Java object.
     *
     * @throws FitsException if the value was invalid or irreparable.
     *
     * @see                  Bitpix#forValue(int)
     * @see                  #getHeaderValue()
     */
    public static Bitpix forValue(int ival, boolean allowRepair) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the standard BITPIX object for the given Java array ID. The array ID is the same letter code as Java uses
     * for identifying ptrimitive array types. For example a Java array of <code>long[][]</code> has a class name of
     * <code>J[[</code>, so so the array ID for <code>long</code> arrays is <code>J</code>.
     *
     * @param  id            The Java letter ID for arrays of the underlying primitive type. E.g. <code>J</code> for
     *                           <code>long</code>.
     *
     * @return               The standard BITPIX enum corresponding to the data type.
     *
     * @throws FitsException if the data type is unknown or does not have a BITPIX ewquivalent.
     */
    public static Bitpix forArrayID(char id) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
