package resources_API.testUtils_API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.MultiPartSpecification;

import java.io.PrintStream;
import java.util.List;

public class FileUploadLoggingFilter implements Filter {
    private final PrintStream printStream;


    public FileUploadLoggingFilter(PrintStream printStream) {
        this.printStream = printStream;
    }




    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {

        // Log request details
        printStream.println("Request method:\t" + requestSpec.getMethod());
        printStream.println("Request URI:\t" + requestSpec.getURI());

        // Log headers
        printStream.println("Headers:\t\t" + requestSpec.getHeaders());

        // Log form parameters
        if (requestSpec.getFormParams() != null && !requestSpec.getFormParams().isEmpty()) {
            printStream.println("Form params:\t" + requestSpec.getFormParams());
        }

        // Log multipart info without binary content
        List<MultiPartSpecification> multiParts = requestSpec.getMultiPartParams();
        if (multiParts != null && !multiParts.isEmpty()) {
            printStream.println("Multiparts:");
            for (MultiPartSpecification multiPart : multiParts) {
                printStream.println("\t\t\t\tControl name = " + multiPart.getControlName() +
                                  ", File name = " + multiPart.getFileName() +
                                  ", Mime type = " + multiPart.getMimeType() +
                                  " [BINARY CONTENT EXCLUDED]");
            }
        }

        // Execute the request
        Response response = ctx.next(requestSpec, responseSpec);

        // Log response details
        printStream.println("HTTP/1.1 " + response.getStatusCode() + " " + response.getStatusLine());
        printStream.println("Response headers:\t" + response.getHeaders());
        printStream.println("Response body:");

        // Pretty print JSON response
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(JsonParser.parseString(response.getBody().asString()));
            printStream.println(prettyJson);
        } catch (Exception e) {
            // If JSON parsing fails, print as-is
            printStream.println(response.getBody().asString());
        }

        return response;
    }
}
