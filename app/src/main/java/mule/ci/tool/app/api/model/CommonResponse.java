package mule.ci.tool.app.api.model;

import java.util.List;
import java.util.Map;

public class CommonResponse {

	private Integer total;
	
    private List<Map<String,Object>> data;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}
}
