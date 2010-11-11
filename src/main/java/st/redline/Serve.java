package st.redline;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.AbstractHandler;

public class Serve {

	private static final String DEFAULT_OBJECT_TO_SERVE = "Serve";

	// App will be available on localhost:8080
	public static void main(String[] args) throws Exception {
		String className = (args.length != 0) ? args[0] : DEFAULT_OBJECT_TO_SERVE;

		Server server = new Server();
		Connector connector=new SocketConnector();
		connector.setPort(8080);
		server.setConnectors(new Connector[]{connector});

		server.setHandler(handle(instanceOf(className)));
		server.start();
		server.join();
	}

	private static ProtoObject instanceOf(String className) {
		tryInitializeClass(className);
		return Smalltalk.classNamed(className);
	}

	private static void tryInitializeClass(String className) {
		try {
			Class.forName("st.redline." + className);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Handler handle(final ProtoObject aClass) {
		System.out.println("handle(" + aClass + ")");
		return new AbstractHandler() {
			public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
				System.out.println(request);
				Request base_request = (request instanceof Request) ? (Request) request:HttpConnection.getCurrentConnection().getRequest();
				base_request.setHandled(true);

				// TODO.jcl Handle dispatch of requests to an instance of aClass.
//				ProtoObject object = aClass.$send("new");
//				if (object != null) {
//					ProtoObject result = object.$send("call:");
//					if (result != null) {
//						// handle unpack of result and put into http response object.
//					} else {
//						// 'call:' didnt respond with object.
//						// make error response.
//					}
//				} else {
//					// don't have instance of 'aClass'
//					// make error response.
//				}

				response.setContentType("text/html");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println("<h1>Hello from Redline Smalltalk</h1>");
			}
		};
	}
}