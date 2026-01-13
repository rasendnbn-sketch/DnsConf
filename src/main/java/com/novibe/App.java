package com.novibe;


import com.novibe.common.DnsTaskRunner;
import com.novibe.common.config.EnvironmentVariables;
import com.novibe.common.util.Log;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class App {
    static void main() {
        String provider = EnvironmentVariables.DNS.toUpperCase();

        String commonsBasePackage = "com.novibe.common";
        // ОТЛАДКА: выведем РОВНО то, что прочитано
        System.out.println("RAW DNS from env: '" + rawDns + "'");
        System.out.println("RAW length: " + rawDns.length());
        System.out.println("RAW bytes: " + java.util.Arrays.toString(rawDns.getBytes()));
        System.out.println("UPPERCASE: '" + provider + "'");
        //
        String dnsBasePackage = switch (provider) {
            case "CLOUDFLARE" -> "com.novibe.dns.cloudflare";
            case "NEXTDNS" -> "com.novibe.dns.next_dns";
            default -> {
                Log.fail(
                "Unsupported DNS provider! Must be CLOUDFLARE or NEXTDNS. Was: " + provider);
                System.exit(1);
                yield null;
            }
        };

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(dnsBasePackage, commonsBasePackage);

        DnsTaskRunner runner = context.getBean(DnsTaskRunner.class);
        runner.run();
    }
}
