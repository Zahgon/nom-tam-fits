package nom.tam.image.compression;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
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
import nom.tam.fits.FitsException;
import nom.tam.fits.Header;
import nom.tam.fits.compression.algorithm.api.ICompressOption;
import nom.tam.fits.compression.algorithm.api.ICompressorControl;
import nom.tam.fits.compression.algorithm.quant.QuantizeOption;
import nom.tam.fits.compression.algorithm.rice.RiceCompressOption;
import nom.tam.fits.compression.provider.CompressorProvider;
import nom.tam.fits.header.Compression;
import nom.tam.fits.header.Standard;
import nom.tam.image.ImageTiler;
import nom.tam.image.StandardImageTiler;
import nom.tam.image.compression.hdu.CompressedImageHDU;
import nom.tam.util.ArrayDataOutput;
import nom.tam.util.ArrayFuncs;
import nom.tam.util.type.ElementType;

/**
 * Class to extract individually compressed tiles from a compressed image. This class supports the FITS 3.0 standard and
 * up, and will stream the results to a provided {@link nom.tam.util.ArrayDataOutput}.
 *
 * @see nom.tam.image.compression.hdu.CompressedImageHDU
 */
public class CompressedImageTiler implements ImageTiler {

    private static final Logger LOGGER = Logger.getLogger(CompressedImageTiler.class.getName());

    static final int DEFAULT_BLOCK_SIZE = 32;

    /**
     * Increment the offset within the position array. Note that we never look at the last index since we copy data a
     * block at a time and not byte by byte.
     *
     * @param  start   The starting corner values.
     * @param  current The current offsets.
     * @param  lengths The desired dimensions of the subset.
     * @param  steps   The amount to increment by.
     *
     * @return         <code>true</code> if the current array was changed
     */
    static boolean incrementPosition(int[] start, int[] current, int[] lengths, int[] steps) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Easily testable static function to ensure the next requested segment of a Tile fits.
     *
     * @param  position  The current position.
     * @param  length    The requested length.
     * @param  dimension The dimension of the current axis.
     *
     * @return           True if valid, False otherwise.
     */
    static boolean isValidSegment(final int position, final int length, final int dimension) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private final CompressedImageHDU compressedImageHDU;

    private final List<String> columnNames = new ArrayList<>();

    /**
     * Only constructor. This will pull commonly accessed elements (header, data) from the HDU.
     *
     * @param compressedImageHDU The compressed Image HDU.
     */
    public CompressedImageTiler(final CompressedImageHDU compressedImageHDU) {
        this.compressedImageHDU = compressedImageHDU;
        init();
    }

    void init() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    void addColumn(final String column) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Fill the subset.
     *
     * @param  output          The stream to be written to.
     * @param  imageDimensions The pixel dimensions of the full image (uncompressed and before slicing).
     * @param  corners         The pixel indices of the corner of the image.
     * @param  lengths         The pixel dimensions of the subset.
     * @param  steps           The pixel amount between values.
     *
     * @throws IOException     if the underlying stream failed
     * @throws FitsException   if any header values cannot be retrieved, or the dimensions are incorrect.
     */
    void getTile(final ArrayDataOutput output, final int[] imageDimensions, final int[] corners, final int[] lengths, final int[] steps) throws IOException, FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Obtain the multidimensional decompressed array of values for the tile at the given position.
     *
     * @param  positions      The location to obtain the tile.
     * @param  tileDimensions The N-dimensional array of a full tile.
     *
     * @return                N-dimensional array of values.
     *
     * @throws FitsException  For any header read errors.
     */
    Object getDecompressedTileData(final int[] positions, final int[] tileDimensions) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    int[] getTileIndexes(final int[] pixelPositions, final int[] tileDimensions) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Decompress the data at row <code>rowNumber</code> and column <code>columnIndex</code>.
     *
     * @param  columnIndex   The column containing the expected compressed data.
     * @param  row           The desired row data.
     *
     * @return               Object array.
     *
     * @throws FitsException If there is no array, or it cannot be decompressed.
     */
    Object decompressRow(final int columnIndex, final Object[] row) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Decompress the given ByteBuffer into a primitive class based Buffer.
     *
     * @param  row        The row array data.
     * @param  compressed The compressed data.
     *
     * @return            Buffer instance. Never null.
     */
    Buffer decompressIntoBuffer(final Object[] row, final ByteBuffer compressed) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    ICompressorControl getCompressorControl(final ElementType<? extends Buffer> elementType) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    ICompressOption initCompressionOption(final ICompressOption option, final int bytePix) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    ElementType<Buffer> getBaseType() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Ensure the Buffer has data that can be used. Tests can override.
     *
     * @param  buffer The buffer to check.
     *
     * @return        True if there is an array, even an empty one. False otherwise.
     */
    boolean hasData(final Buffer buffer) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Obtain the row for the given number. Tests can override this to alleviate the need to create an HDU.
     *
     * @param  positions      The corners of the desired tile.
     * @param  tileDimensions The dimensions of a (de)compressed tile.
     *
     * @return                Object array row.
     *
     * @throws FitsException  If the row doesn't exist, or cannot be read.
     */
    Object[] getRow(final int[] positions, final int[] tileDimensions) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    int getRowNumber(final int[] tileIndexes) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Obtain the starting corner within the starting tile.
     *
     * @param  corners The pixel corners specified.
     *
     * @return         Multidimensional array of pixel corners
     */
    int[] getTileOffsets(final int[] corners, final int[] tileDimensions) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public Object getCompleteImage() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public void getTile(Object output, int[] corners, int[] lengths) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public void getTile(Object output, int[] corners, int[] lengths, int[] steps) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public Object getTile(int[] corners, int[] lengths) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    void initRowOption(final ICompressOption option, final Object[] row) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    Header getHeader() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private String getQuantizAlgorithmName() {
        return getHeader().getStringValue(Compression.ZQUANTIZ);
    }

    private String getCompressionAlgorithmName() {
        return getHeader().getStringValue(Compression.ZCMPTYPE);
    }

    int getBlockSize() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Obtain the dimension count of this image (ZNAXIS). Tests can override.
     *
     * @return integer of dimension count. Never null.
     */
    int getNumberOfDimensions() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    int getZBitPix() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    int getImageAxisLength(final int axis) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    int[] getTableDimensions() throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Obtain the full image dimensions of the image that is represented by this compressed binary table.
     *
     * @return The image dimensions.
     */
    int[] getImageDimensions() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    int getTileHeight() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    int getTileWidth() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    int[] getTileDimensions() throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    int getTileDimensionLength(final int dimension) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    int getTileSize() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
