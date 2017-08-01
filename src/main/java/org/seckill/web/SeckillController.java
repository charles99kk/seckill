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

@Controller//���@Service @component 
@RequestMapping("/seckill")
//url:/ģ�K/�YԴ/{id}/���� http://localhost:8080/seckill/seckill/list
//ȥ��@RequestMapping("/seckill")��Ϊhttp://localhost:8080/seckill/list
public class SeckillController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService seckillService;
	
	@RequestMapping(value="/list",method = RequestMethod.GET)
	//����URL
	public String list(Model model){
		//�@ȡ�б��
		List<Seckill> list = seckillService.getSeckillList();
		model.addAttribute("list",list);
		//list.jsp + model = ModelAndView
		return "list";//spring-web.xml������prefix��suffix ->WEB-INF/jsp/list.jsp
		
	}
	
	//
	@RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
	//Ҳ�ɲ�д@PathVariable("seckillId")��Ĭ����ʶ���������û�д��
	/*ʹ�û������ͽ������ݻ����ð�װ�ࣿ
		ʹ��@PathVariableʱע�����㣺
		1��������������ʹ�û�������
		2�����û�������ʱ����defaultValueֵ
		�Ƽ�ʹ�ð�װ��*/
	public String detail(@PathVariable("seckillId")Long seckillId,Model model){
		//nullʱ��redirect��list.jsp
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
	
	/*ajax json   RequestMethod.POSTֱ�������ַ��Ч
	 * @ResponseBody ��springmvc����@ResponseBody��ʱ��ὫSeckillResult<Exposer>����ֵ��װ��json

	 * produces = {"application/json;charset=UTF-8"}���json�е�������������
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
		/*�����requestpattern��û�����cookie killPhoneʱ��springMVC �ᱨ��required = false��ʾkillPhone���Ǳ��룬�Ͳ��ᱨ����֤�߼��������*/
		//springMVC valid
		if(phone == null){
			return new SeckillResult<SeckillExecution>(false,"δע��");
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
