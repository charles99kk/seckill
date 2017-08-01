package org.seckill.web;

import java.util.Date;
import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillExeception;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller//類似@Service @component 
@RequestMapping("/seckill")
//url:/模塊/資源/{id}/細分 http://localhost:8080/seckill/seckill/list
//去掉@RequestMapping("/seckill")变为http://localhost:8080/seckill/list
public class SeckillController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService seckillService;
	
	@RequestMapping(value="/list",method = RequestMethod.GET)
	//二級URL
	public String list(Model model){
		//獲取列表頁
		List<Seckill> list = seckillService.getSeckillList();
		model.addAttribute("list",list);
		//list.jsp + model = ModelAndView
		return "list";//spring-web.xml配置了prefix和suffix ->WEB-INF/jsp/list.jsp
		
	}
	
	//
	@RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
	//也可不写@PathVariable("seckillId")，默认能识别出，但最好还写出
	/*使用基本类型接收数据还是用包装类？
		使用@PathVariable时注意两点：
		1：参数接收类型使用基本类型
		2：不用基本类型时，给defaultValue值
		推荐使用包装类*/
	public String detail(@PathVariable("seckillId")Long seckillId,Model model){
		//null时，redirect到list.jsp
		if(seckillId == null){
			return "redirect:/seckill/list";
		}
		Seckill seckill = seckillService.getById(seckillId);
		if(seckill == null){
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill",seckill);
		return "detail";
	}
	
	/*ajax json   RequestMethod.POST直接输入地址无效
	 * @ResponseBody 当springmvc看到@ResponseBody的时候会将SeckillResult<Exposer>返回值封装成json

	 * produces = {"application/json;charset=UTF-8"}解决json中的数据乱码问题
	 * */
	@RequestMapping(value = "/{seckillId}/exposer",
			method = RequestMethod.POST,
			produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	//public void/*TODO*/ exposer(Long seckillId){
	public SeckillResult<Exposer> exposer(Long seckillId){

		SeckillResult<Exposer> result;
		try{
			Exposer exposer =seckillService.exportSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true,exposer);
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			result = new SeckillResult<Exposer>(false,e.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value = "/{seckillId}/{md5}/execution",
			method = RequestMethod.POST,
			produces = {"application/json;charset=UTF-8"})
	public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId")Long seckillId,
												   @PathVariable("md5")String md5,
												   @CookieValue(value = "killPhone",required = false)Long phone){
		/*请求的requestpattern中没有这个cookie killPhone时，springMVC 会报错，required = false表示killPhone不是必须，就不会报错，验证逻辑放入程序*/
		//springMVC valid
		if(phone == null){
			return new SeckillResult<SeckillExecution>(false,"未注册");
		}
		SeckillResult<SeckillExecution> result;
		try{
			SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);
			return new SeckillResult<SeckillExecution>(true,execution);
		} catch (SeckillCloseException e) {
			SeckillExecution execution = new SeckillExecution(seckillId,SeckillStatEnum.REPEAT_KILL);
			return new SeckillResult<SeckillExecution>(false,execution);
		} catch (RepeatKillExeception e) {
			SeckillExecution execution = new SeckillExecution(seckillId,SeckillStatEnum.END);
			return new SeckillResult<SeckillExecution>(false,execution);
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			SeckillExecution execution = new SeckillExecution(seckillId,SeckillStatEnum.INNER_ERROR);
			return new SeckillResult<SeckillExecution>(false,execution);
		}
	} 
	@RequestMapping(value = "/time/now",
				method = RequestMethod.GET)
	@ResponseBody
	public SeckillResult<Long> time(){
		Date now = new Date();
		return new SeckillResult(true,now.getTime());
	}

}
