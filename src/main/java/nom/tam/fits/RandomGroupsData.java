package nom.tam.fits;

/*-
 * #%L
 * nom.tam.fits
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
import java.io.IOException;
import java.lang.reflect.Array;
import nom.tam.fits.header.Bitpix;
import nom.tam.fits.header.Standard;
import nom.tam.util.ArrayDataInput;
import nom.tam.util.ArrayDataOutput;
import nom.tam.util.ArrayFuncs;
import nom.tam.util.Cursor;
import nom.tam.util.FitsEncoder;

/**
 * Random Groups data. The use of random groups is discouraged, even by the FITS standard. Some old radio data may be
 * packaged in this format. Thus apart from provided limited support for reading such data, users should not create
 * random groups anew. {@link BinaryTable} offers a much more flexible and capable way for storing an ensemble of
 * parameters, arrays, and more.
 * <p>
 * Random groups are instantiated as a two-dimensional array of objects. The first dimension of the array is the number
 * of groups. The second dimension is 2. The first object in every row is a one dimensional parameter array. The second
 * element is the n-dimensional data array.
 *
 * @see BinaryTable
 */
public class RandomGroupsData extends Data {

    private int groups;

    private Object[] sampleRow;

    private Object[][] dataArray;

    /**
     * Create the equivalent of a null data element.
     */
    public RandomGroupsData() {
        dataArray = new Object[0][];
    }

    /**
     * Constructor used by RandomGroupsHDU only...
     *
     * @param gcount    The number of parameter groups
     * @param sampleRow The 2-element array of a sample group.
     *
     * @since           1.18
     */
    RandomGroupsData(int gcount, Object[] sampleRow) {
        this();
        groups = gcount;
        this.sampleRow = sampleRow;
    }

    /**
     * Create a RandomGroupsData object using the specified object to initialize the data array.
     *
     * @param  x                        The initial data array. This should a two-d array of objects as described above.
     *
     * @throws IllegalArgumentException if the second array dimension is specified and it is not 2, or if the parameter
     *                                      arrya is not 1-dimensional, or if the parameter and data types differ.
     */
    public RandomGroupsData(Object[][] x) throws IllegalArgumentException {
        dataArray = x == null ? new Object[0][] : x;
        groups = dataArray.length;
        if (groups > 0) {
            if (dataArray[0].length != 2) {
                throw new IllegalArgumentException("Second array dimension must be 2");
            }
            if (Array.getLength(ArrayFuncs.getDimensions(dataArray[0][0])) != 1) {
                throw new IllegalArgumentException("Expected 1D parameter array.");
            }
            if (dataArray[0][1] != null) {
                Class<?> pbase = ArrayFuncs.getBaseClass(dataArray[0][0]);
                Class<?> dbase = ArrayFuncs.getBaseClass(dataArray[0][1]);
                if (pbase != dbase) {
                    throw new IllegalArgumentException("Mismatched parameters and data types (" + pbase.getName() + " vs " + dbase.getName() + ")");
                }
            }
            sampleRow = new Object[2];
            sampleRow[0] = ArrayFuncs.deepClone(dataArray[0][0]);
            sampleRow[1] = ArrayFuncs.deepClone(dataArray[0][1]);
        }
    }

    /**
     * Returns the Java class of the the parameter and data array elements.
     *
     * @return The java class of the parameter and data elements.
     *
     * @since  1.18
     */
    public Class<?> getElementType() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the dimensions of the grouped parameters
     *
     * @return The dimensions of the parameters or -1 if not defined.
     *
     * @see    #getDataDims()
     *
     * @since  1.18
     */
    public int getParameterCount() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the dimensions of the grouped data
     *
     * @return The dimensions of the parameters, or <code>null</code> if not defined.
     *
     * @see    #getParameterCount()
     *
     * @since  1.18
     */
    public int[] getDataDims() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void fillHeader(Header h) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    protected long getTrueSize() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    protected void loadData(ArrayDataInput in) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    protected Object[][] getCurrentData() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public Object[][] getData() throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @SuppressWarnings({ "resource", "deprecation" })
    @Override
    public void write(ArrayDataOutput str) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @SuppressWarnings("deprecation")
    @Override
    public RandomGroupsHDU toHDU() throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the image component stored in the specified group.
     *
     * @param  group                          The zero-based group index
     *
     * @return                                The image array for the specified group
     *
     * @throws ArrayIndexOutOfBoundsException if the group index is out of bounds
     * @throws FitsException                  if the deferred data could not be loaded.
     *
     * @see                                   RandomGroupsHDU#getParameter(String, int)
     *
     * @since                                 1.19
     */
    public Object getImage(int group) throws ArrayIndexOutOfBoundsException, FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    Object getParameterArray(int group) throws ArrayIndexOutOfBoundsException, FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
