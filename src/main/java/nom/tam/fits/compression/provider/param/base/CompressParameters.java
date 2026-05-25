package nom.tam.fits.compression.provider.param.base;

import nom.tam.fits.BinaryTable;
import nom.tam.fits.BinaryTableHDU;
import nom.tam.fits.FitsException;
import nom.tam.fits.Header;
import nom.tam.fits.HeaderCard;
import nom.tam.fits.HeaderCardException;
import nom.tam.fits.compression.provider.param.api.ICompressColumnParameter;
import nom.tam.fits.compression.provider.param.api.ICompressHeaderParameter;
import nom.tam.fits.compression.provider.param.api.ICompressParameters;
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
import static nom.tam.fits.header.Standard.TTYPEn;

/**
 * (<i>for internal use</i>) A set of {@link CompressParameter}s that are bundled together, typically because they are
 * parameters that all link to the same {@link nom.tam.fits.compression.algorithm.api.ICompressOption}
 *
 * @see CompressParameter
 */
public abstract class CompressParameters implements ICompressParameters, Cloneable {

    @Override
    public void addColumnsToTable(BinaryTableHDU hdu) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    protected CompressParameters clone() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public void setTileIndex(int index) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public void getValuesFromColumn(int index) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public void getValuesFromHeader(Header header) throws HeaderCardException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public void initializeColumns(Header header, BinaryTable binaryTable, int size) throws HeaderCardException, FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public void initializeColumns(int size) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public void setValuesInColumn(int index) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public void setValuesInHeader(Header header) throws HeaderCardException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private Object getNullableColumn(Header header, BinaryTable binaryTable, String columnName) throws HeaderCardException, FitsException {
        for (int i = 1; i <= binaryTable.getNCols(); i++) {
            HeaderCard card = header.getCard(TTYPEn.n(i));
            if (card != null) {
                if (card.getValue().trim().equals(columnName)) {
                    return binaryTable.getColumn(i - 1);
                }
            }
        }
        return null;
    }

    /**
     * Retuens the subset of parameters from within, which are recorded in compressed table columns along with the
     * compressed data.
     *
     * @return the subset of parameters that are recorded in compressed table columns.
     *
     * @see    #headerParameters()
     */
    protected ICompressColumnParameter[] columnParameters() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the subset of parameters from within, which are recorded in the header of the compressed HDU.
     *
     * @return the subset of parameters that are recorded in the compressed HDU's header.
     *
     * @see    #columnParameters()
     */
    protected abstract ICompressHeaderParameter[] headerParameters();
}
