package nom.tam.fits.compression.algorithm.rice;

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
import nom.tam.fits.compression.algorithm.api.ICompressOption;
import nom.tam.fits.compression.provider.param.api.ICompressParameters;
import nom.tam.fits.compression.provider.param.rice.RiceCompressParameters;
import nom.tam.util.type.ElementType;

/**
 * Options to the Rice compression algorithm. When compressing tables and images using the Rice algorithm, users can
 * control how exactly the compression is perfomed. When reading compressed FITS files, these options will be set
 * automatically based on the header values recorded in the compressed HDU.
 *
 * @see nom.tam.image.compression.hdu.CompressedImageHDU#setCompressAlgorithm(String)
 * @see nom.tam.image.compression.hdu.CompressedImageHDU#getCompressOption(Class)
 */
public class RiceCompressOption implements ICompressOption {

    /**
     * the default block size to use in bytes
     */
    public static final int DEFAULT_RICE_BLOCKSIZE = 32;

    /**
     * the default BYTEPIX value
     */
    public static final int DEFAULT_RICE_BYTEPIX = ElementType.INT.size();

    /**
     * Set of valid BYTEPIX values
     */
    private static final int[] VALID_BYTEPIX = { 1, 2, 4, 8 };

    /**
     * Set of valid BLOCKSIZE values
     */
    private static final int[] VALID_BLOCKSIZE = { 16, 32 };

    /**
     * The parameters that represent settings for this option in the FITS headers and/or compressed data columns
     */
    private RiceCompressParameters parameters;

    /**
     * Shared configuration across copies
     */
    private final Config config;

    /**
     * Creates a new set of options for Rice compression.
     */
    public RiceCompressOption() {
        config = new Config();
        parameters = new RiceCompressParameters(this);
    }

    @Override
    public RiceCompressOption copy() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the currently set block size.
     *
     * @return the block size in bytes.
     *
     * @see    #setBlockSize(int)
     */
    public final int getBlockSize() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * REturns the currently set BYTEPIX value
     *
     * @return the BYTEPIX value.
     *
     * @see    #setBytePix(int)
     */
    public final int getBytePix() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public RiceCompressParameters getCompressionParameters() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public boolean isLossyCompression() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Sets a new block size to use
     *
     * @param  value                    the new block size in bytes
     *
     * @return                          itself
     *
     * @throws IllegalArgumentException if the value is not 16 or 32.
     *
     * @see                             #getBlockSize()
     */
    public RiceCompressOption setBlockSize(int value) throws IllegalArgumentException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Sets a new BYTEPIX value to use.
     *
     * @param  value                    the new BYTEPIX value. It is currently not checked for validity, so use
     *                                      carefully.
     *
     * @return                          itself
     *
     * @throws IllegalArgumentException if the value is not 1, 2, 4, or 8.
     *
     * @see                             #getBytePix()
     */
    public RiceCompressOption setBytePix(int value) throws IllegalArgumentException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public void setParameters(ICompressParameters parameters) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public RiceCompressOption setTileHeight(int value) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public RiceCompressOption setTileWidth(int value) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public <T> T unwrap(Class<T> clazz) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Stores configuration in a way that can be shared and modified across enclosing option copies.
     *
     * @author Attila Kovacs
     *
     * @since  1.18
     */
    private static final class Config {

        private int bytePix = DEFAULT_RICE_BYTEPIX;

        private int blockSize = DEFAULT_RICE_BLOCKSIZE;
    }
}
