#include <iostream>
//#include "tools.h"

#include <vector>

int main()
{
	std::cout << "Hello World!" << std::endl;


	std::vector<int> listOfNums;
	listOfNums.push_back(1);
	listOfNums.push_back(2);
	listOfNums.push_back(4);

	for(int i = 0; i < listOfNums.size(); i++)
	{
		std::cout << i << " = " << listOfNums[i] << std::endl;
	}

	//std::string str = "Hi from the tools print function";
	//myPrint(str);
	return 0;
}

