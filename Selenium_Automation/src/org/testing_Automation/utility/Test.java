package org.testing_Automation.utility;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class Test {

	public static void main(String[] args) {
		HashMap<String, String> hsmMappingData = new HashMap<>();
		hsmMappingData.put("KEY1", "KEY1");
		hsmMappingData.put("KEY2", "KEY2");
		
		Iterator it = hsmMappingData.entrySet().iterator();
		while (it.hasNext())
		{
			System.out.println("In while loop");
			Object pair = (Object) it.next();
			
				if (hsmMappingData.containsKey("KEY1"))
				{
					
					for (int i = 0; i < hsmMappingData.size(); i++)
					{
						System.out.println("inside button for loop" + i);

						String sValue = (String)hsmMappingData.get("KEY1");
						if (sValue.equalsIgnoreCase("KEY1"))
						{
							System.out.println("inside if loop radio button"
							        );

							/*((WebElement) oCheckBox.get(i)).click();*/

							break;

						}else{
							System.out.println("inside else loop radio button"
							        );

							//((WebElement) oCheckBox.get(i)).click();
							
						}
						//Thread.sleep(50000);

					}
					System.out.println("after ravi radio button");

				}
				else if (hsmMappingData.containsKey("KEY3"))
				{
										System.out.println("after ravi radio button");
				}

	}

}
}
