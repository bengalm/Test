package me.jamesj.portscanner;

/**
 * Created by James on 16/07/2016.
 * (c) JamesJ, or respective owners, 2016
 */
public class Bootstrap {

    public static void main(String[] args) {
        for (String sr:
                args) {
            System.out.println(sr+">>>");
        }

        PortScanner portScanner = new PortScanner();
        portScanner.start(args);
    }



}
