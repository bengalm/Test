package me.jamesj.portscanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.net.util.SubnetUtils;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by James on 18/08/2016.
 * (c) JamesJ, or respective owners, 2016
 */
public class PortScanner {

    public void start(String[] args){
        PrintStream out = System.out;
        out.println("Powered by bengal Em:406894560@qq.com");
        AtomicInteger openPorts = new AtomicInteger(0);
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
      //  String ipOrCidr = "";
        int thred;
        if (args[2].isEmpty())
            thred=200;
        else
            thred=Integer.parseInt(args[2]);


        String ipOrCidr =args[0]+"/24";
        //int port;
        int port=Integer.parseInt(args[1]);
        Integer level = 0;
        final List<String> arguments = new ArrayList<>();
        if(args.length > 1){
            for(int i = 0; i < args.length; i++) arguments.add(args[i]);
        }

        if(arguments.contains("--superbeefymachine")){
            out.println("----------------------------------------------------------------");
            out.println("                            WARNING                             ");
            out.println("            YOU HAVE CHOSEN TO LIVE DANGEROUSLY                 ");
            out.println("          I TOO, LIKE TO LIVE DANGEROUSLY, HOWEVER              ");
            out.println("         I (JamesJ) AM NOT RESPONSIBLE FOR ANY FINES            ");
            out.println("        FIRES, OR ANYTHING, THAT THIS APPLICATION MAY           ");
            out.println("       INCUR, AS --SUPERBEEFYMACHINE WILL MELT THROUGH          ");
            out.println("         YOUR CPU. PRESS CTRL + C TO CANCEL NOW. XOX            ");
            out.println("----------------------------------------------------------------");
        }

        while (true) {
            //如果有值跳出
            if (port!=0&&!ipOrCidr.isEmpty())
                break;
            if (level == 1) {
                if (port==0)
                out.println("Please enter an port to scan for. Formats accepted: 22");
                else
                    break;
            } else {
                if (ipOrCidr.isEmpty()) {
                    out.println("Please enter an IP/CIDR to scan. Formats accepted: 198.0.0.1/32 or 192.0.0.0/24");
                }
                else{
                    level=1;
                    break;
                }
            }
            try {
                String line = bufferRead.readLine();
                if (level == 0) {
                    if (!line.matches("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\\/([0-9]|[1-2][0-9]|3[0-2]))$")) {
                        out.println("Invalid IP/CIDR format. Please try again.");
                    } else {
                        ipOrCidr = line;
                        level = 1;
                    }
                } else {
                    try {
                        port = Integer.valueOf(line);
                        if (!(port < 1 || port > 65535)) {
                            break;
                        }
                        out.println("Invalid port format. Please try again.");
                    } catch (NumberFormatException e) {
                        out.println("Invalid port format. Please try again.");
                    }
                }
            } catch (Exception ex) {
                System.exit(-1);
            }
        }
        if (ipOrCidr.isEmpty() || port == -1) {
            System.exit(-1);
        }
        long start = System.currentTimeMillis();
        final ExecutorService es;

        if(arguments.contains("--superbeefymachine")){
            es = Executors.newCachedThreadPool();
        } else {
            es = Executors.newFixedThreadPool(thred);//多少线程
        }

        // Find the relevant IPs we're going to scan.得到ips
        SubnetUtils utils = new SubnetUtils(ipOrCidr);
        String[] ips = utils.getInfo().getAllAddresses();
        log("Scanning " + ips.length + " IP addresses for openings on port " + port + "...");


        List<Callable<String>> collection = new ArrayList<>();
        for (final String ip : ips) {
            final int finalPort = port;
            Callable<String> runnable = () -> {
                Boolean open = false;
                try {
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(ip, finalPort), 200);
                    socket.close();
                    open = true;
                    openPorts.set(openPorts.get() + 1);
                } catch (Exception ex) {
                    open = false;
                }
                log("Port " + finalPort + " is " + (open ? "open" : "closed") + " on " + ip);
                if (open && arguments.contains("--mcscan")) {
                    String motd = getMOTD(ip);
                    if (motd != null && !motd.isEmpty()) {
                        log(" MOTD » " + motd);
                    }
                }
                return "";
            };
            collection.add(runnable);
        }
        // Time to scan. Rip CPU if you had --superbeefymachine enabled.
        try {
            es.invokeAll(collection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log("Scanned " + ips.length + " IPs in " + (System.currentTimeMillis() - start) + "ms. A total of " + openPorts.get() + " open ports were found on port " + port);
    //关闭
        es.shutdown();
       /* while (true) {
            if (es.isTerminated()) {
                System.out.println("结束了！");
                break;
            }
            Thread.sleep(20000);
        }*/

    }


    private void log(String msg) {
        System.out.println(msg);
//端口开放 的话就记录
        if(msg.indexOf("open")!=-1) {

            try (FileWriter fw = new FileWriter("out.log", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                String regex = "\\d+\\.\\d+\\.\\d+\\.\\d+";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(msg);
                if (matcher.find()) {
                    out.println(matcher.group(0));
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getMOTD(String host) {
        FutureTask<String> task = new FutureTask<>(() -> {
            BufferedReader reader;
            URL url = new URL("https://mcapi.ca/query/" + host + "/motd");
            final URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5000);
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String content = "";
            String line;
            while ((line = reader.readLine()) != null) {
                content += line;
            }
            final Map<String, Object> map = new Gson().fromJson(content, new TypeToken<Map<String, Object>>() {
            }.getType());
            if (!map.containsKey("motd")) {
                return null;
            }
            return map.get("motd").toString();
        });
        new Thread(task).run();
        try {
            return task.get();
        } catch (Exception e) {
            return null;
        }
    }
}
