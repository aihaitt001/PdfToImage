import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 * @author ovo
 * @decription 利用apache的pdfbox将PDF转为图像
 * @classname Example
 * @date 2018/10/18 15:08
 */
public class Example {
    public static void main(String[] args) throws Exception {

        try {
            File pdfsrc = new File("D:/pdf");
            if (!pdfsrc.exists()) {
                System.out.println("D盘下没有文件夹pdf");
                pdfsrc.mkdirs();
                System.out.println("创建D:/pdf成功");
            }
            //把所有PDF文件过滤出来
            File[] filelist = getFileList(pdfsrc);

            for (File file : filelist) {
                System.out.println("扫描到文件:" + file.getName());
            }
            for (File file : filelist) {
                System.out.println(file.getName() + "开始转化");
                convertPdfToImage(file);
                System.out.println(file.getName() + "转化完成");
            }
        } catch (Exception e) {
            System.out.println("未知错误:" + e.toString());
            System.out.println("未知错误:请联系作者.");
        }
    }

    static public File[] getFileList(File pdfsrc) {

        File[] filelist = pdfsrc.listFiles(new NameFilter());
        while (filelist.length == 0) {
            System.out.println("在D:/pdf下没有pdf文件,请在把PDF放入文件夹后,输入\"1\"重新扫描");

            int inputint = 0;
            try {
                inputint = new Scanner(System.in).nextInt();
            }catch(Exception e){
                System.out.println("不要输入其他的值,输入\"1\"");
            }
            if (inputint == 1) {
                filelist = pdfsrc.listFiles(new NameFilter());
            }
        }
        return filelist;
    }

    static class NameFilter implements FilenameFilter {

        private final String type = ".pdf";

        @Override
        public boolean accept(File dir, String name) {

            if (name != null && name.toLowerCase().endsWith(type)) {
                return true;
            } else {
                return false;
            }
        }
    }

    static List<File> convertPdfToImage(File file) throws Exception {
        PDDocument doc = PDDocument.load(file);
        PDFRenderer renderer = new PDFRenderer(doc);
        List<File> fileList = new ArrayList<File>();

        for (int i = 0; i < doc.getNumberOfPages(); i++) {

            String filename = file.getParent() + "/" + file.getName().substring(0, file.getName().lastIndexOf(".")) + (i + 1) + ".jpg";
            File fileTemp = new File(filename); // jpg or png
            BufferedImage image = renderer.renderImageWithDPI(i, 200);
            // 200 is sample dots per inch.
            ImageIO.write(image, "JPEG", fileTemp); // JPEG or PNG
            fileList.add(fileTemp);
            System.out.println("第" + (i + 1) + "页完成...");
        }
        doc.close();

        return fileList;
    }
}