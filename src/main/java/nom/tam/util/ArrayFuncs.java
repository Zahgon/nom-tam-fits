package nom.tam.util;

/*
 * #%L
 * nom.tam FITS library
 * %%
 * Copyright (C) 2004 - 2024 nom-tam-fits
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
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import nom.tam.util.array.MultiArrayCopier;
import nom.tam.util.type.ElementType;

/**
 * (<i>for internal use</i>) Varioys static functions for handling arrays. Generally these routines attempt to complete
 * without throwing errors by ignoring data they cannot understand.
 */
public final class ArrayFuncs {

    private static final Logger LOG = Logger.getLogger(ArrayFuncs.class.getName());

    private ArrayFuncs() {
    }

    /**
     * Retuens a copy of the input array with the order of elements reversed.
     *
     * @param  index the input array
     *
     * @return       a copy of the input array in reversed order
     */
    public static int[] getReversed(int... index) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Perform an array copy with an API similar to System.arraycopy(), specifying the number of values to jump to the
     * next read.
     *
     * @param src     The source array.
     * @param srcPos  Starting position in the source array.
     * @param dest    The destination array.
     * @param destPos Starting position in the destination data.
     * @param length  The number of array elements to be read.
     * @param step    The number of jumps to the next read.
     *
     * @since         1.18
     */
    public static void copy(Object src, int srcPos, Object dest, int destPos, int length, int step) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns a string description of the array type and (regular) dimensions.
     *
     * @return   a description of an array (presumed rectangular).
     *
     * @param  o The array to be described.
     */
    public static String arrayDescription(Object o) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * @deprecated   Use {@link FitsEncoder#computeSize(Object)} instead.
     *
     * @param      o the object
     *
     * @return       the number of bytes in the FITS binary representation of the object or 0 if the object has no FITS
     *                   representation. (Also elements not known to FITS will count as 0 sized).
     */
    @Deprecated
    public static long computeLSize(Object o) {
        return FitsEncoder.computeSize(o);
    }

    /**
     * @deprecated   Use {@link FitsEncoder#computeSize(Object)} instead.
     *
     * @param      o the object
     *
     * @return       the number of bytes in the FITS binary representation of the object or 0 if the object has no FITS
     *                   representation. (Also elements not known to FITS will count as 0 sized).
     */
    @Deprecated
    public static int computeSize(Object o) {
        return (int) computeLSize(o);
    }

    /**
     * Converts a numerical array to a specified element type. This method supports conversions only among the primitive
     * numeric types, and {@link ComplexValue} types (as of version 1.20). When converting primitive arrays to complex
     * values, the trailing dimension must be 2, corresponding to the real and imaginary components of the complex
     * values stored.
     *
     * @param  array   a numerical array of one or more dimensions
     * @param  newType the desired output type. This should be one of the class descriptors for primitive numeric data,
     *                     e.g., <code>double.class</code>, or else a {@link ComplexValue} type (also supported as of
     *                     1.20).
     *
     * @return         a new array with the requested element type, which may also be composed of {@link ComplexValue}
     *                     types as of version 1.20.
     *
     * @see            #convertArray(Object, Class, boolean)
     */
    public static Object convertArray(Object array, Class<?> newType) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Converts a numerical array to a specified element type, returning the original if type conversion is not needed.
     * This method supports conversions only among the primitive numeric types originally. Support for
     * {@link ComplexValue} types was added as of version 1.20. When converting primitive arrays to complex values, the
     * trailing dimension must be 2, corresponding to the real and imaginary components of the complex values stored.
     *
     * @param  array   a numerical array of one or more dimensions
     * @param  newType the desired output type. This should be one of the class descriptors for primitive numeric data,
     *                     e.g., <code>double.class</code> r else a {@link ComplexValue} type (also supported as of
     *                     1.20).
     * @param  reuse   If the original (rather than a copy) should be returned when possible for the same type.
     *
     * @return         a new array with the requested element type, or possibly the original array if it readily matches
     *                     the type and <code>reuse</code> is enabled.
     *
     * @see            #convertArray(Object, Class)
     * @see            #convertArray(Object, Class, Quantizer)
     */
    public static Object convertArray(Object array, Class<?> newType, boolean reuse) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Copy one array into another. This function copies the contents of one array into a previously allocated array.
     * The arrays must agree in type and size.
     *
     * @param      original                 The array to be copied.
     * @param      copy                     The array to be copied into. This array must already be fully allocated.
     *
     * @throws     IllegalArgumentException if the two arrays do not match in type or size.
     *
     * @deprecated                          (<i>for internal use</i>)
     */
    @Deprecated
    public static void copyArray(Object original, Object copy) throws IllegalArgumentException {
        Class<? extends Object> cl = original.getClass();
        if (!cl.isArray()) {
            throw new IllegalArgumentException("original is not an array");
        }
        if (!copy.getClass().equals(cl)) {
            throw new IllegalArgumentException("mismatch of types: " + cl.getName() + " vs " + copy.getClass().getName());
        }
        int length = Array.getLength(original);
        if (Array.getLength(copy) != length) {
            throw new IllegalArgumentException("mismatch of sizes: " + length + " vs " + Array.getLength(copy));
        }
        if (original instanceof Object[]) {
            Object[] from = (Object[]) original;
            Object[] to = (Object[]) copy;
            for (int index = 0; index < length; index++) {
                copyArray(from[index], to[index]);
            }
        } else {
            System.arraycopy(original, 0, copy, 0, length);
        }
    }

    /**
     * Copy an array into an array of a different type. The dimensions and dimensionalities of the two arrays should be
     * the same.
     *
     * @param array The original array.
     * @param mimic The array mimicking the original.
     */
    public static void copyInto(Object array, Object mimic) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Curl an input array up into a multi-dimensional array.
     *
     * @param  input                 The one dimensional array to be curled.
     * @param  dimens                The desired dimensions
     *
     * @return                       The curled array.
     *
     * @throws IllegalStateException if the size of the input does not match the specified dimensions.
     */
    public static Object curl(Object input, int... dimens) throws IllegalStateException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns a deep clone of an array (in one or more dimensions) or a standard clone of a scalar. The object may
     * comprise arrays of any primitive type or any Object type which implements Cloneable. However, if the Object is
     * some kind of collection, such as a {@link java.util.List}, then only a shallow copy of that object is made. I.e.,
     * deep refers only to arrays.
     *
     * @return   a new object, with a copy of the original.
     *
     * @param  o The object (usually an array) to be copied.
     */
    public static Object deepClone(Object o) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Given an array of arbitrary dimensionality .
     *
     * @return       the array flattened into a single dimension.
     *
     * @param  input The input array.
     */
    public static Object flatten(Object input) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Clone an Object if possible. This method returns an Object which is a clone of the input object. It checks if the
     * method implements the Cloneable interface and then uses reflection to invoke the clone method. This can't be done
     * directly since as far as the compiler is concerned the clone method for Object is protected and someone could
     * implement Cloneable but leave the clone method protected. The cloning can fail in a variety of ways which are
     * trapped so that it returns null instead. This method will generally create a shallow clone. If you wish a deep
     * copy of an array the method deepClone should be used.
     *
     * @param  o The object to be cloned.
     *
     * @return   the clone
     */
    public static Object genericClone(Object o) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * This routine returns the base array of a multi-dimensional array. I.e., a one-d array of whatever the array is
     * composed of. Note that arrays are not guaranteed to be rectangular, so this returns o[0][0]....
     *
     * @param  o the multi-dimensional array
     *
     * @return   base array of a multi-dimensional array.
     */
    public static Object getBaseArray(Object o) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * This routine returns the base class of an object. This is just the class of the object for non-arrays.
     *
     * @param  o array to get the base class from
     *
     * @return   the base class of an array
     */
    public static Class<?> getBaseClass(Object o) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * This routine returns the size of the base element of an array.
     *
     * @param  o The array object whose base length is desired.
     *
     * @return   the size of the object in bytes, 0 if null, or -1 if not a primitive array.
     */
    public static int getBaseLength(Object o) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Find the dimensions of an object. This method returns an integer array with the dimensions of the object o which
     * should usually be an array. It returns an array of dimension 0 for scalar objects and it returns -1 for dimension
     * which have not been allocated, e.g., <code>int[][][] x = new
     * int[100][][];</code> should return [100,-1,-1].
     *
     * @param  o The object to get the dimensions of.
     *
     * @return   the dimensions of an object
     */
    public static int[] getDimensions(Object o) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Create an array of a type given by new type with the dimensionality given in array.
     *
     * @return         the new array with same dimensions
     *
     * @param  array   A possibly multidimensional array to be converted.
     * @param  newType The desired output type. This should be one of the class descriptors for primitive numeric data,
     *                     e.g., double.type.
     */
    public static Object mimicArray(Object array, Class<?> newType) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Convenience method to check a generic Array object.
     *
     * @param  o The Array to check.
     *
     * @return   True if it's empty, False otherwise.
     *
     * @since    1.18
     */
    public static boolean isEmpty(final Object o) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * @return       Count the number of elements in an array.
     *
     * @param      o the array to count the elements
     *
     * @deprecated   May silently underestimate size if number is &gt; 2 G.
     */
    @Deprecated
    public static int nElements(Object o) {
        return (int) nLElements(o);
    }

    /**
     * Allocate an array dynamically. The Array.newInstance method does not throw an error and silently returns a
     * null.throws an OutOfMemoryError if insufficient space is available.
     *
     * @param  cl   The class of the array.
     * @param  dims The dimensions of the array.
     *
     * @return      The allocated array.
     */
    public static Object newInstance(Class<?> cl, int... dims) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the total number of elements contained in an array of one or more dimensions.
     *
     * @return       Count the number of elements in an array.
     *
     * @param      o the array to count elements in
     *
     * @deprecated   Use the more aptly named {@link #countElements(Object)} instead.
     */
    @Deprecated
    public static long nLElements(Object o) {
        return countElements(o);
    }

    /**
     * Returns the total number of elements contained in an array of one or more dimensions.
     *
     * @return   Count the number of elements in an array.
     *
     * @param  o the array to count elements in
     *
     * @since    1.18
     */
    public static long countElements(Object o) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Reverse an integer array. This can be especially useful when dealing with an array of indices in FITS order that
     * you wish to have in Java order.
     *
     * @return         the reversed array.
     *
     * @param  indices the array to reverse
     */
    public static int[] reverseIndices(int... indices) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Checks that an array has a regular structure, with a consistent shape and element types, and returns the regular
     * array size or else throws an exeption. Optionally, it will also throw an exception if any or all all elements are
     * <code>null</code>.
     *
     * @param  o                        An array object
     * @param  allowNulls               If we should tolerate <code>null</code> entries.
     *
     * @return                          the regular shape of the array with sizes along each array dimension.
     *
     * @throws NullPointerException     if the argument is <code>null</code>.
     * @throws IllegalArgumentException if the array contains mismatched elements in size, or contains <code>null</code>
     *                                      values.
     * @throws ClassCastException       if the array contain a heterogeneous collection of different element types.
     *
     * @since                           1.18
     */
    public static int[] checkRegularArray(Object o, boolean allowNulls) throws NullPointerException, IllegalArgumentException, ClassCastException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Converts complex value(s) count to <code>float[2]</code> or <code>double[2]</code> or arrays thereof, which
     * maintain the shape of the original input array (if applicable).
     *
     * @param  o                        one of more complex values
     * @param  decimalType              <code>float.class</code> or <code>double.class</code> (all other values default
     *                                      to as if <code>double.class</code> was used.
     *
     * @return                          an array of <code>float[2]</code> or <code>double[2]</code>, or arrays thereof.
     *
     * @throws IllegalArgumentException if the argument is not suitable for conversion to complex values.
     *
     * @see                             #decimalsToComplex(Object)
     *
     * @since                           1.18
     */
    public static Object complexToDecimals(Object o, Class<?> decimalType) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Converts real-valued arrays of even element count to a {@link ComplexValue} or arrays thereof. The size and shape
     * is otherwise maintained, apart from coalescing pairs of real values into <code>ComplexValue</code> objects.
     *
     * @param  array                    an array of <code>float</code> or <code>double</code> elements containing an
     *                                      even number of elements at the last dimension
     *
     * @return                          one of more complex values
     *
     * @throws IllegalArgumentException if the argument is not suitable for conversion to complex values.
     *
     * @see                             #decimalsToComplex(Object, Object)
     * @see                             #complexToDecimals(Object, Class)
     *
     * @since                           1.18
     */
    public static Object decimalsToComplex(Object array) throws IllegalArgumentException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Converts separate but matched real valued arrays, containing the real an imaginary parts respectively, to
     * {@link ComplexValue} or arrays thereof. The size and shape of the matching input arrays is otherwise maintained,
     * apart from coalescing pairs of real values into <code>ComplexValue</code> objects.
     *
     * @param  re                       an array of <code>float</code> or <code>double</code> elements containing the
     *                                      real parts of the complex values.
     * @param  im                       a matching array of the same type and shape as the real parts which contains the
     *                                      imaginary parts of the complex values.
     *
     * @return                          one of more complex values
     *
     * @throws IllegalArgumentException if the argument is not suitable for conversion to complex values.
     *
     * @see                             #decimalsToComplex(Object)
     *
     * @since                           1.20
     */
    public static Object decimalsToComplex(Object re, Object im) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private static void decimalToInteger(Object from, Object to, Quantizer q) {
        if (from instanceof Object[]) {
            Object[] a = (Object[]) from;
            Object[] b = (Object[]) to;
            for (int i = 0; i < a.length; i++) {
                decimalToInteger(a[i], b[i], q);
            }
        } else {
            for (int i = Array.getLength(from); --i >= 0; ) {
                long l = q.toLong(from instanceof double[] ? ((double[]) from)[i] : ((float[]) from)[i]);
                if (to instanceof byte[]) {
                    ((byte[]) to)[i] = (byte) l;
                } else if (to instanceof short[]) {
                    ((short[]) to)[i] = (short) l;
                } else if (to instanceof int[]) {
                    ((int[]) to)[i] = (int) l;
                } else {
                    ((long[]) to)[i] = l;
                }
            }
        }
    }

    private static void integerToDecimal(Object from, Object to, Quantizer q) {
        if (from instanceof Object[]) {
            Object[] a = (Object[]) from;
            Object[] b = (Object[]) to;
            for (int i = 0; i < a.length; i++) {
                integerToDecimal(a[i], b[i], q);
            }
        } else {
            for (int i = Array.getLength(from); --i >= 0; ) {
                double d = q.toDouble(Array.getLong(from, i));
                if (to instanceof float[]) {
                    ((float[]) to)[i] = (float) d;
                } else {
                    ((double[]) to)[i] = d;
                }
            }
        }
    }

    /**
     * Converts a numerical array to a specified element type, returning the original if type conversion is not needed.
     * If the conversion is from decimal to integer type, or vice-versa, an optional quantization may be supplied to to
     * perform the integer-decimal conversion of the elements. This method supports conversions only among the primitive
     * numeric types and also {@link ComplexValue} type.
     *
     * @param  array                    a numerical array of one or more dimensions
     * @param  newType                  the desired output type. This should be one of the class descriptors for
     *                                      primitive numeric data, e.g., <code>double.class</code>, or else a
     *                                      {@link ComplexValue} or {@link ComplexValue.Float}.
     * @param  quant                    optional qunatizer for integer-decimal conversion, or <code>null</code> to use
     *                                      simply rounding.
     *
     * @return                          a new array with the requested element type, or possibly the original array if
     *                                      it readily matches the type and <code>reuse</code> is enabled.
     *
     * @throws IllegalArgumentException if the input is not an array, or its elements are not a supported type or if the
     *                                      new type is not supported.
     *
     * @see                             #convertArray(Object, Class)
     * @see                             #convertArray(Object, Class, Quantizer)
     *
     * @since                           1.20
     */
    public static Object convertArray(Object array, Class<?> newType, Quantizer quant) throws IllegalArgumentException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Obtains a sparse sampling of from an array of one or more dimensions.
     *
     * @param  orig                      the original array
     * @param  step                      the sampling step size along all dimensions for a subsampled slice. A negative
     *                                       value indicates that the sampling should proceed in the reverse direction
     *                                       along every axis.
     *
     * @return                           the requested sampling from the original. The returned array may share data
     *                                       with the original, and so modifications to either may affect the other. The
     *                                       orginal object is returned if it is not an array.
     *
     * @throws IndexOutOfBoundsException if any of the indices for the requested slice are out of bounds for the
     *                                       original. That is if the original does not fully contain the requested
     *                                       slice. Or, if the from and size arguments have differing lengths.
     *
     * @since                            1.20
     *
     * @see                              #sample(Object, int[])
     * @see                              #sample(Object, int[], int[], int[])
     */
    public static Object sample(Object orig, int step) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Obtains a sparse sampling of from an array of one or more dimensions.
     *
     * @param  orig                      the original array
     * @param  step                      the sampling step size along each dimension for a subsampled slice. Negative
     *                                       values indicate that the sampling should proceed in the reverse direction
     *                                       along the given axis.
     *
     * @return                           the requested sampling from the original. The returned array may share data
     *                                       with the original, and so modifications to either may affect the other. The
     *                                       orginal object is returned if it is not an array.
     *
     * @throws IndexOutOfBoundsException if any of the indices for the requested slice are out of bounds for the
     *                                       original. That is if the original does not fully contain the requested
     *                                       slice. Or, if the from and size arguments have differing lengths.
     *
     * @since                            1.20
     *
     * @see                              #sample(Object, int)
     * @see                              #sample(Object, int[], int[], int[])
     */
    public static Object sample(Object orig, int[] step) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Obtains a slice (subarray) from an array of one or more dimensions.
     *
     * @param  orig                      the original array
     * @param  from                      the starting indices for the slice in the original array. It should have at
     *                                       most as many elements as there are array dimensions, but it can also have
     *                                       fewer.
     *
     * @return                           the requested slice from the original. The returned array may share data with
     *                                       the original, and so modifications to either may affect the other. The
     *                                       orginal object is returned if it is not an array.
     *
     * @throws IndexOutOfBoundsException if any of the indices for the requested slice are out of bounds for the
     *                                       original. That is if the original does not fully contain the requested
     *                                       slice. Or, if the from and size arguments have differing lengths.
     *
     * @since                            1.20
     *
     * @see                              #slice(Object, int[], int[])
     * @see                              #sample(Object, int[], int[], int[])
     */
    public static Object slice(Object orig, int[] from) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Obtains a slice (subarray) from an array of one or more dimensions.
     *
     * @param  orig                      the original array
     * @param  from                      the starting indices for the slice in the original array. It should have at
     *                                       most as many elements as there are array dimensions, but it can also have
     *                                       fewer.
     * @param  size                      the size of the slice. Negative values can indicate moving backwards in the
     *                                       original array (but forward in the slice -- resulting in a flipped axis). A
     *                                       <code>null</code> size argument can be used to sample the full original.
     *                                       The slice will end at index <code>from[k] + size[k]</code> in dimension
     *                                       <code>k</code> in the original (not including the ending index). It should
     *                                       have the same number of elements as the <code>from</code> argument.
     *
     * @return                           the requested slice from the original. The returned array may share data with
     *                                       the original, and so modifications to either may affect the other. The
     *                                       orginal object is returned if it is not an array.
     *
     * @throws IndexOutOfBoundsException if any of the indices for the requested slice are out of bounds for the
     *                                       original. That is if the original does not fully contain the requested
     *                                       slice. Or, if the from and size arguments have differing lengths.
     *
     * @since                            1.20
     *
     * @see                              #slice(Object, int[])
     * @see                              #sample(Object, int[], int[], int[])
     */
    public static Object slice(Object orig, int[] from, int[] size) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Obtains a sparse sampling from an array of one or more dimensions.
     *
     * @param  orig                      the original array
     * @param  from                      the starting indices in the original array at which to start sampling. It
     *                                       should have at most as many elements as there are array dimensions, but it
     *                                       can also have fewer. A <code>null</code> argument can be used to sample
     *                                       from the start or end of the array (depending on the direction).
     * @param  size                      the size of the sampled area in the original along each dimension. The
     *                                       signature of the values is irrelevant as the direction of sampling is
     *                                       determined by the step argument. Zero entries can be used to indicate that
     *                                       the full array should be sampled along the given dimension, while a
     *                                       <code>null</code> argument will sample the full array in all dimensions.
     * @param  step                      the sampling step size along each dimension for a subsampled slice. Negative
     *                                       values indicate sampling the original in the reverse direction along the
     *                                       given dimension. 0 values are are automatically bumped to 1 (full
     *                                       sampling), and a <code>null</code> argument is understood to mean full
     *                                       sampling along all dimensions.
     *
     * @return                           the requested sampling from the original. The returned array may share data
     *                                       with the original, and so modifications to either may affect the other. The
     *                                       orginal object is returned if it is not an array.
     *
     * @throws IndexOutOfBoundsException if any of the indices for the requested slice are out of bounds for the
     *                                       original. That is if the original does not fully contain the requested
     *                                       slice. Or, if the from and size arguments have differing lengths.
     *
     * @since                            1.20
     *
     * @see                              #sample(Object, int)
     * @see                              #sample(Object, int[])
     * @see                              #slice(Object, int[], int[])
     */
    public static Object sample(Object orig, int[] from, int[] size, int[] step) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private static Object sample(Object orig, int[] from, int[] size, int[] step, int idx) throws IndexOutOfBoundsException {
        // If leaf, return it as is...
        if (!orig.getClass().isArray() || (from != null && idx == from.length)) {
            return orig;
        }
        int l = Array.getLength(orig);
        int ndim = from == null ? getDimensions(orig).length : from.length;
        // Check if reverse sampling
        boolean isReversed = (step != null && step[idx] < 0);
        int ifrom = from == null ? (isReversed ? l - 1 : 0) : from[idx];
        int isize = size == null ? 0 : Math.abs(size[idx]);
        if (isize == 0) {
            isize = l - ifrom;
        }
        int ito = ifrom + (isReversed ? -isize : isize);
        if (ifrom < 0 || ito < -1 || ifrom >= l || ito > l) {
            throw new IndexOutOfBoundsException("Sampled bounds are out of range for original array");
        }
        int istep = step == null ? 1 : step[idx];
        if (istep == 0) {
            istep = 1;
        }
        int astep = Math.abs(istep);
        int n = Math.abs((isize + astep - 1) / astep);
        Object slice = Array.newInstance(orig.getClass().getComponentType(), n);
        if (!isReversed && ndim == 1 && istep == 1) {
            // Special case for fast in-order slicing along last dim...
            System.arraycopy(orig, ifrom, slice, 0, isize);
        } else {
            // Generic sampling with the parameters...
            for (int i = 0; i < n; i++) {
                Object efrom = Array.get(orig, ifrom + i * istep);
                Array.set(slice, i, sample(efrom, from, size, step, idx + 1));
            }
        }
        return slice;
    }

    /**
     * Converts objects to arrays. If the object is already an array it is returned unchanged. Boxed primitives are
     * returned as primitive arrays of 1. All other objects are wrapped into an array of 1 of the same type.
     * <code>Boolean</code> values are somewhat special and are handled according to the second argument, either to
     * produce a <code>boolean[1]</code> or else a <code>Boolean[1]</code>.
     *
     * @param  o               The object
     * @param  booleanAsObject Whether <code>Boolean</code> values should be converted <code>Boolean[1]</code> instead
     *                             of <code>boolean[1]</code>.
     *
     * @return                 The input object, wrapped into an array as appropriate.
     *
     * @since                  1.21
     */
    public static Object objectToArray(Object o, boolean booleanAsObject) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
