package com.sleepy.zeo.springboot.controller;

import com.sleepy.zeo.springboot.data.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

@Controller
@RequestMapping("/upload")
public class UploadController {

    private static final Log logger = LogFactory.getLog(UploadController.class);

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String upload(HttpServletRequest request, MultipartFile file) throws Exception {
        String upload = request.getSession().getServletContext().getRealPath(Constants.UPLOAD_DIR);
        File uploadFile = new File(upload);
        logger.info("upload path: " + upload);
        if (!uploadFile.exists()) {
            boolean success = uploadFile.mkdir();
            if (!success) {
                throw new Exception("Directory " + upload + " created failed");
            }
        }

        String filename = file.getOriginalFilename();
        file.transferTo(new File(upload + File.separator + filename));
        return "success";
    }
}
