NewsPaper
=========
Description:
Set a chest to be the newstand, chest contents can be duplicated in any users inventory when they type /news
They can only type /news once a day.
One can then use a book and quill to write any quick updates that users can download once a day.

------------------------------------- 

ChangeLog:
  0.2
    - Added centralized message codes
    - Prevented players without news.admin permission breaking the news stand
    - Streamlined the command structure
    - Added help menu
    - Added reload
    - Added reset <-- add menu item & try to reset storageconfig (Reset Names & Dates)
  0.3
    - Added confirmation message to /news clear
    - Added /news clear to help menu
    - Added /news reset
    - Added /news reset to help menu
 
------------------------------------- 

Permissions:
	news.setstand 	- Set or Clear the news stand
	news.admin 		- Access to all commands
 	news.reload 	- Reload NewsPaper
 
Commands:
  /news help		- Display this menu.
	/news 			- Retrieve today's Newspaper.	
	/news set 		- Set the News Stand.
	/news clear		- Clear NewsStand location.
	/news reset		- Clear today's recipients.	
	/news reload	- Reload the news config.




