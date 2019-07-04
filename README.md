# camConvertorApp
final project PostPc HUJI

here are my implementations for launch Activity (I included lot of animations for it to be fascinating...maybe too much) and the settings Activity
included with the data base of the App (most important is the API level in  FrequenciesViewModel.class - I included documentations 
trying to explain what I did)

at this time, it works well running on my phone and android emulator, but maybe we will have to throw away some of the animations because it can 
do some version problems at compile time. so dont hesitate to delete some of them from Main activity..

points to take care of
1.think how to deal with large amount of types and signs. almost impossible to show all of them on settings menu... 
maybe load from DB on cloud or web server
... meanwhile, I added all of the conversion types (frequencies) by hand from site https://unicode-table.com/en/#0073

2.see the API for Room sqlite which stores all the user's types (given as Object of 3 fields:type, source (sign), target (sign) 
in FrequenciesViewModel class and AppDataBase class


