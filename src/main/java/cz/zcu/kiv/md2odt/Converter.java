package cz.zcu.kiv.md2odt;

import com.vladsch.flexmark.parser.Parser;
import cz.zcu.kiv.md2odt.document.Document;
import cz.zcu.kiv.md2odt.document.odfdom.OdfdomDocument;
import cz.zcu.kiv.md2odt.filler.*;
import cz.zcu.kiv.md2odt.filler.md.FlexMarkFiller;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Converts Markdown into OpenDocument.
 *
 * @author Patrik Harag
 * @version 2017-04-29
 */
public class Converter {

    private static final String DEFAULT_TEMPLATE = "/default-template.odt";
    private static final Predicate<URL> DEFAULT_RESOURCE_POLICY = (url) -> true;
    private static final long DEFAULT_RESOURCES_LIMIT = Long.MAX_VALUE;

    private static final Pattern SOURCE_FILE_PATTERN = Pattern.compile(".*\\.md");

    private Source input;
    private InputStream template;
    private OutputStream output;

    private FlexMarkExtensions extensions = new FlexMarkExtensions();

    private long resourcesLimit = DEFAULT_RESOURCES_LIMIT;
    private Predicate<URL> resourcesPolicy;

    // source

    /**
     * Sets Markdown source.
     *
     * @param md source
     * @return this
     */
    public Converter setInput(String md) {
        this.input = new SourceString(md);
        return this;
    }

    /**
     * Sets Markdown source.
     *
     * @param in source
     * @param charset charset
     * @return this
     */
    public Converter setInput(InputStream in, Charset charset) throws IOException {
        this.input = new SourceCharStream(in, charset);
        return this;
    }

    /**
     * Sets Markdown source.
     *
     * @param file source
     * @param charset charset
     * @return this
     */
    public Converter setInput(File file, Charset charset) throws IOException {
        return setInput(new FileInputStream(file), charset);
    }

    /**
     * Sets Markdown source.
     *
     * @param file source
     * @param charset charset
     * @return this
     */
    public Converter setInput(Path file, Charset charset) throws IOException {
        return setInput(file.toFile(), charset);
    }

    /**
     * Sets input as a zip. The zip must contain exactly one Markdown source and
     * any number of resources (images).
     *
     * @param in zip
     * @param charset charset of the Markdown source
     * @return this
     */
    public Converter setInputZip(InputStream in, Charset charset) throws IOException {
        this.input = new SourceZip(in, charset, SOURCE_FILE_PATTERN);
        return this;
    }

    /**
     * See {@link #setInput(InputStream, Charset)}.
     *
     * @param file zip
     * @param charset charset
     * @return this
     */
    public Converter setInputZip(File file, Charset charset) throws IOException {
        return setInputZip(new FileInputStream(file), charset);
    }

    /**
     * See {@link #setInput(InputStream, Charset)}.
     *
     * @param file zip
     * @param charset charset
     * @return this
     */
    public Converter setInputZip(Path file, Charset charset) throws IOException {
        return setInputZip(file.toFile(), charset);
    }

    // template

    /**
     * Sets ODT or OTT template.
     * If not set, default template will be used.
     *
     * @param template template
     * @return this
     */
    public Converter setTemplate(InputStream template) throws IOException {
        this.template = template;
        return this;
    }

    /**
     * See {@link #setTemplate(InputStream)}.
     *
     * @param file template
     * @return this
     */
    public Converter setTemplate(File file) throws IOException {
        return setTemplate(new FileInputStream(file));
    }

    /**
     * See {@link #setTemplate(InputStream)}.
     *
     * @param file template
     * @return this
     */
    public Converter setTemplate(Path file) throws IOException {
        return setTemplate(file.toFile());
    }

    // output

    /**
     * Sets ODT output.
     *
     * @param out output stream
     * @return this
     */
    public Converter setOutput(OutputStream out) throws IOException {
        this.output = out;
        return this;
    }

    /**
     * Sets ODT output.
     *
     * @param file output stream
     * @return this
     */
    public Converter setOutput(File file) throws IOException {
        return setOutput(new FileOutputStream(file, false));
    }

    /**
     * Sets ODT output.
     *
     * @param file output stream
     * @return this
     */
    public Converter setOutput(Path file) throws IOException {
        return setOutput(file.toFile());
    }

    // resources

    /**
     * Sets total max size of resources in bytes.
     *
     * @param resourcesLimit max size in bytes
     * @return this
     */
    public Converter setResourcesLimit(long resourcesLimit) {
        this.resourcesLimit = resourcesLimit;
        return this;
    }

    /**
     * Sets resource filter.
     * For example it is possible to filter remote resources.
     *
     * @param predicate filter
     * @return this
     */
    public Converter setResourcesPolicy(Predicate<URL> predicate) {
        this.resourcesPolicy = predicate;
        return this;
    }

    // extensions

    /**
     * Enables given extension.
     *
     * @param extension extension
     * @return this
     */
    public Converter enableExtension(String extension) {
        extensions.add(extension);
        return this;
    }

    /**
     * Enables given extensions.
     *
     * @param collection collection of extensions
     * @return this
     */
    public Converter enableExtensions(Collection<String> collection) {
        collection.forEach(this::enableExtension);
        return this;
    }

    /**
     * Enables given extensions.
     *
     * @param array array of extensions
     * @return this
     */
    public Converter enableExtensions(String... array) {
        return enableExtensions(Arrays.asList(array));
    }

    /**
     * Enables all extensions.
     *
     * @return this
     */
    public Converter enableAllExtensions() {
        return enableExtensions(Extensions.LIST);
    }

    // convert

    /**
     * Converts Markdown source into OpenDocument.
     *
     * @throws IOException
     */
    public void convert() throws IOException {
        if (input == null)
            throw new IllegalArgumentException("Input not set");

        if (output == null)
            throw new IllegalArgumentException("Output not set");

        Document document = new OdfdomDocument(getTemplate());

        Parser parser = Parser.builder().extensions(extensions.getExtensions()).build();
        Filler filler = new FlexMarkFiller(parser);
        filler.fill(input.getSource(), getResourceManager(), document);
        document.save(output);
    }

    private ResourceManager getResourceManager() {
        Predicate<URL> predicate = (resourcesPolicy != null)
                ? resourcesPolicy
                : DEFAULT_RESOURCE_POLICY;

        long limit = (resourcesLimit >= 0)
                ? resourcesLimit
                : DEFAULT_RESOURCES_LIMIT;

        return new ResourceManagerImpl(input.getResources(), predicate, limit);
    }

    private InputStream getTemplate() {
        if (template != null)
            return template;
        else
            return System.class.getResourceAsStream(DEFAULT_TEMPLATE);
    }

}
