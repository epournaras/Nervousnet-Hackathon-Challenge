# [Nervousnet Hackathon Challenge](https://github.com/epournaras/Nervousnet-Hackathon-Challenge/blob/master/tutorial/tutorial_v1.1.pdf)

Dates: 22-23.04.2016

Please register [here](http://phplist.inn.ac/?p=subscribe&id=2).

The goal of this Hackathon session is to develop __summarization functions__ that add noise to sensor data to protect privacy. However, when the summarized data are collected, analytics such the the summation and average aggregation functions, should be performed with a satisfactory accuracy. Therefore, a summarization function can be evaluated in the light of this trade-off: __privacy-preservation vs. accuracy in data analytics__. 

A summarizaiton function receives as input a vector with the raw sensitive data and provides as output a vector with summarized values of the same size. The entropy/diversity of the summarized data should be lower than the one of the raw data. Performance is measured as follows: 

* __Privacy-preservation__ is measured with _relative error of raw-sumamrized data_. For several users, the average of these errors measures the collective privacy-preservation. 
* __Accuracy__ in analytics is measured with the _relative error of raw-summarized aggregated data_. 

More information about the performance metrics can be found [here](https://github.com/epournaras/Nervousnet-Hackathon-Challenge/blob/master/papers/Self-regulatory-information-sharing-in-participatory-social-sensing.pdf) and [here](https://github.com/epournaras/Nervousnet-Hackathon-Challenge/blob/master/presentations/information-sharing.pdf). Particpants do not need to implement these metrics themselves, instead they can use the [Challenge Analyzer](https://github.com/epournaras/Nervousnet-Hackathon-Challenge/tree/master/ChallengeAnalyser) to see how their summarization algorithm performs. The output files can be submitted [here](http://hackathon.inn.ac/submission/).

The application scenario of this hackathon challenge is the following: you are given the smart meter power consumption readings of __1000 consumers__ during __a winter and summer month__. For each consumer and day, __48 measurements__ are recorded. There are the raw data that need to be sumamarized. Try out one or more summarization functions over consumers' data so that you maximize the average local error of summarized data, while you minimze the error of aggregated data. 

This repository provides you all the necessary utilities and APIs to implement the summarization functions. You do not need to worry about how you can load the data, how you output the data, in what format, etc. All these details are handled by the [challengeLib.jar](https://github.com/epournaras/Nervousnet-Hackathon-Challenge/blob/master/ChallengeLib/challengeLib.jar). More specifically, all required utilities can be found [here](https://github.com/epournaras/Nervousnet-Hackathon-Challenge/tree/master/ChallengeLib/src/nervousnet/challenge). We also provide [an example](https://github.com/epournaras/Nervousnet-Hackathon-Challenge/tree/master/ChallengeLib/src/test) of a summarization function that is based on the k-means algorithm. 

To participate in the Nervousnet Hackathon Challenge, follow the following 6 steps:

1. Create the class ```MySummarizationFunction.java```. 
2. Use the method ```exportClonedRawValues(...)``` of [```Loader.java```](https://github.com/epournaras/Nervousnet-Hackathon-Challenge/blob/master/ChallengeLib/src/nervousnet/challenge/Loader.java) to retrieve all the required data.
3. Implement your summarization function within ```MySummarizationFunction.java``` using the returned values of ```exportClonedRawValues(...)``` in step 2. [Here](https://github.com/epournaras/Nervousnet-Hackathon-Challenge/blob/master/ChallengeLib/src/test/Tester.java) is an example.
4. Use the ```initOutputMap()``` of [```Dumper.java```](https://github.com/epournaras/Nervousnet-Hackathon-Challenge/blob/master/ChallengeLib/src/nervousnet/challenge/Dumper.java) to initialize and prepare the output of the summarization function. 
5. Add the summarized data in the output of the [```Dumper.java```](https://github.com/epournaras/Nervousnet-Hackathon-Challenge/blob/master/ChallengeLib/src/nervousnet/challenge/Dumper.java).
6. Call the method ```dump()``` in [```Dumper.java```](https://github.com/epournaras/Nervousnet-Hackathon-Challenge/blob/master/ChallengeLib/src/nervousnet/challenge/Dumper.java). 

More information about how to implement summarization functions can be found in [this tutorial](https://github.com/epournaras/Nervousnet-Hackathon-Challenge/blob/master/tutorial/tutorial_v1.1.pdf). 


Background
---

This hackathon is inspired by a work that envisions information sharing as a participatory and democratic supply-demand system self-regulated in a bottom-up fashion by citizens. 

[>E. Pournaras, J. Nikolic, P. Velasquez, M. Trovati, N. Bessis and D. Helbing, _Self-regulatory Information Sharing in Participatory Social Sensing_, The European Physical Journal Data Science, 5:14, 2016 © SpringerOpen](http://epjdatascience.springeropen.com/articles/10.1140/epjds/s13688-016-0074-4)

__Self-regulatory Information Sharing in Participatory Social Sensing__

_Participation in social sensing applications is challenged by privacy threats. Large-scale access to citizens’ data allow surveillance and discriminatory actions that may result in segregation phenomena in society. On the contrary are the benefits of accurate computing analytics required for more informed decision-making, more effective policies and regulation of techno-socio-economic systems supported by ‘Internet-of Things’ technologies. In contrast to earlier work that either focuses on privacy protection or Big Data analytics, this paper proposes a self-regulatory information sharing system that bridges this gap. This is achieved by modeling information sharing as a supply-demand system run by computational markets. On the supply side lie the citizens that make incentivized but self-determined decisions about the level of information they share. On the demand side stand data aggregators that provide rewards to citizens to receive the required data for accurate analytics. The system is empirically evaluated with two real-world datasets from two application domains: (i) Smart Grids and (ii) mobile phone sensing. Experimental results quantify trade-offs between privacy-preservation, accuracy of analytics and costs from the provided rewards under different experimental settings. Findings show a higher privacy-preservation that depends on the number of participating citizens and the type of data summarized. Moreover, analytics with summarization data tolerate high local errors without a significant influence on the global accuracy. In other words, local errors cancel out. Rewards can be optimized to be fair so that citizens with more significant sharing of information receive higher rewards. All these findings motivate a new paradigm of truly decentralized and ethical data analytics._
