import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        for (String arg : args) {
            System.out.println(arg);
        }
        String doadLoadPath = args[0];// "/home/zhangli/mydisk-2t/scholarly-bigdata/acl/acl-anthonogy-crawl-pdf-2021-01-10";
        String metadataPath = args[1];//"/home/zhangli/mydisk-2t/scholarly-bigdata/acl/acl-anthonogy-crawl-pdf-2021-01-10/metadata.txt";
        String aclBibFile = args[2]; // "/home/zhangli/mydisk-2t/repo/ACLWebReader/ACLWebReader/src/anthology.bib";
        int needSkip = 0;
        if (args.length >= 4 && args[3] != null) {
            needSkip = Integer.parseInt(args[3]);
        }

        analyzeAnthology a = new analyzeAnthology(aclBibFile);
        for (int i = needSkip; i <= a.MyACLPaperInfo.length; i++) {
            System.out.println(i);
            if (a.MyACLPaperInfo[i].url == null)
                continue;
            try {
                ACLwebReader.downloadPapers(a.MyACLPaperInfo[i].url, a.MyACLPaperInfo[i].name + ".pdf", doadLoadPath);
                ACLwebReader.getInformation("https://www.aclweb.org/anthology/P19-1001", metadataPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        for (int i = 13333; i < a.MyACLPaperInfo.length; i++) {
//            System.out.println(i);
//            if (a.MyACLPaperInfo[i].url == null)
//                continue;
//            ACLwebReader.getAbstract(a.MyACLPaperInfo[i].url, "abstract", a.MyACLPaperInfo[i].name);
//        }
//        ACLwebReader.getInformation("https://www.aclweb.org/anthology/P19-1001", metadataPath);
//        //collect abstract
//        for (int i = 0; i < a.MyACLPaperInfo.length; i++) {
//            if (a.MyACLPaperInfo[i].article == 1) {
//                if (a.MyACLPaperInfo[i].url == null)
//                    continue;
//                ACLwebReader.getAbstract(a.MyACLPaperInfo[i].url, a.MyACLPaperInfo[i].name, "abstract1");
//            }
//        }
    }
}
