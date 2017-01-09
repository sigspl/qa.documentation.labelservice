package org.fiware.qa.documentation.measurements;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "documentation" path)
 */
@Path("documentation")
public class Documentation {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
	
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/getit")
    public String getIt() {
        return "Got it!";
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/scoretable")
    public String scoretable() {
        return "***SCORETABLE***";
    }
    
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/label/{enabler}")
    public String label(@PathParam("enabler") String enabler) {
        return "A++";
    }
    
    

    /*
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String scoring() {
        return "a;b;c;d!";
    }
    */
}
