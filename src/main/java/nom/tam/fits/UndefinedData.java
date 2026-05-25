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
import nom.tam.fits.header.Bitpix;
import nom.tam.fits.header.Standard;
import nom.tam.util.ArrayDataInput;
import nom.tam.util.ArrayDataOutput;
import nom.tam.util.ArrayFuncs;
import nom.tam.util.Cursor;
import nom.tam.util.FitsEncoder;

/**
 * A container for unknown binary data types. We can still retrieve the data as a <code>byte[]</code> array, we just
 * don't know how to interpret it ourselves. This class makes sure we don't break when we encouter HDUs that we don't
 * (yet) support, such as HDU types defined by future FITS standards.
 *
 * @see UndefinedHDU
 */
public class UndefinedData extends Data {

    private static final String XTENSION_UNKNOWN = "UNKNOWN";

    // private static final Logger LOG = getLogger(UndefinedData.class);
    private Bitpix bitpix = Bitpix.BYTE;

    private int[] dims;

    private int byteSize = 0;

    private byte[] data;

    private int pCount = 0;

    private int gCount = 1;

    private String extensionType = XTENSION_UNKNOWN;

    /**
     * Creates a new empty container for data of unknown type based on the provided FITS header information.
     *
     * @param      h             The FITS header corresponding to the data segment in the HDU
     *
     * @throws     FitsException if there wan an error accessing or interpreting the provided header information.
     *
     * @deprecated               (<i>for internal use</i>). Visibility will be reduced to the package level in the
     *                               future.
     */
    @Deprecated
    public UndefinedData(Header h) throws FitsException {
        extensionType = h.getStringValue(Standard.XTENSION, XTENSION_UNKNOWN);
        int naxis = h.getIntValue(Standard.NAXIS);
        dims = new int[naxis];
        int size = naxis > 0 ? 1 : 0;
        for (int i = 1; i <= naxis; i++) {
            dims[naxis - i] = h.getIntValue(Standard.NAXISn.n(i));
            size *= dims[naxis - i];
        }
        pCount = h.getIntValue(Standard.PCOUNT);
        size += pCount;
        gCount = h.getIntValue(Standard.GCOUNT);
        if (gCount > 1) {
            size *= h.getIntValue(Standard.GCOUNT);
        }
        bitpix = Bitpix.fromHeader(h);
        size *= bitpix.byteSize();
        byteSize = size;
    }

    /**
     * @deprecated                          (<i>for internal use</i>). Users should always construct known data types.
     *                                          Reduce visibility to the package level.
     *
     * @param      x                        object to create the hdu from
     *
     * @throws     IllegalArgumentException If the object is not an array or contains elements that do not have a known
     *                                          binary size.
     */
    @Deprecated
    public UndefinedData(Object x) throws IllegalArgumentException {
        byteSize = (int) FitsEncoder.computeSize(x);
        dims = ArrayFuncs.getDimensions(x);
        data = new byte[byteSize];
        ArrayFuncs.copyInto(x, data);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void fillHeader(Header head) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    protected byte[] getCurrentData() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    protected long getTrueSize() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the FITS extension type as stored by the XTENSION keyword in the FITS header.
     *
     * @return The value used for the XTENSION keyword in the FITS header
     *
     * @since  1.19
     */
    public final String getXtension() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the FITS element type as a Bitpux value.
     *
     * @return The FITS Bitpix value for the type of primitive data element used by this data
     *
     * @since  1.19
     */
    public final Bitpix getBitpix() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the size of the optional parameter space as stored by the PCOUNT keyword in the FITS header.
     *
     * @return The element count of the optional parameter space accompanying the main data, as stored by the PCOUNT
     *             header value.
     *
     * @since  1.19
     */
    public final int getParameterCount() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the number of repeated (data + parameter) groups in this data object
     *
     * @return The number of repeated data + parameter blocks, as stored by the GCOUNT header value.
     *
     * @since  1.19
     */
    public final int getGroupCount() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the dimensionality of the data (if any), in Java array index order. That is, The value for NAXIS1 is the
     * last value in the returned array
     *
     * @return the regular dimensions of the data in Java index order (that is NAXIS1 is the last entry in the array),
     *             or possibly <code>null</code> if no dimensions have been defined.
     */
    public final int[] getDimensions() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public byte[] getData() throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    protected void loadData(ArrayDataInput in) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @SuppressWarnings({ "resource", "deprecation" })
    @Override
    public void write(ArrayDataOutput o) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    @SuppressWarnings("deprecation")
    public UndefinedHDU toHDU() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
