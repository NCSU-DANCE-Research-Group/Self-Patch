We have created scripts for this process  

1.  extract_full_vector.py  
e.g. `python extract_full_vector.py`  
It calls FrequencyProcessing.class to extract syscall vectors from raw traces within folders given by apps.txt and the specified directories.  

2.  segment_train_vector.sh  
e.g. `./segment_train_vector`  
It divides the resulting vector file into another file for train set within the specified directories.
