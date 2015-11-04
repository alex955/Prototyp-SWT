package videoshop.controller;

import java.io.BufferedOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import videoshop.model.Disc;
import videoshop.model.Picture;
import videoshop.model.PictureRepository;
import videoshop.model.Disc.DiscType;

@Controller
public class FileUploadController {
	
	public PictureRepository repo;
	
	@Autowired
	public FileUploadController(PictureRepository repo){
		this.repo = repo;
	}
    
    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public  String provideUploadInfo() {
        return "Upload";
    }
    
    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("name") String name, 
            @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream = 
                        new BufferedOutputStream(new FileOutputStream(new File(name)));
                stream.write(bytes);
                stream.close();
                Picture pic = new Picture();
                pic.setName(name);
                pic.setPic(bytes);
                repo.save(pic);
                return "You successfully uploaded " + name + "!";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }
    
    /**
     * Beispiel URL /showUpload?name=namesdesbildes
     * @param modelmap
     * @param id
     * @return
     */
    @RequestMapping(value="/showUpload", method=RequestMethod.GET)
    public String showUpload(
    		ModelMap modelmap, 
    		@RequestParam("name") String nameOfPic){
    	Optional<Picture> picFromDatabase = repo.findByName(nameOfPic);
    	if(picFromDatabase.isPresent())
    	{
    		modelmap.addAttribute("picPath", picFromDatabase.get().getfile());
    	}
    	return "viewupload";
    }

}
