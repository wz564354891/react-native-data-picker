//
//  CitiesDataTool.m
//  ChooseLocation
//
//  Created by Sekorm on 16/10/25.
//  Copyright © 2016年 HY. All rights reserved.
//

#import "CitiesDataTool.h"
#import "AddressItem.h"
#import "ProvinceModel.h"
#import "CityModel.h"
#import "TownModel.h"

@interface CitiesDataTool ()
@property (nonatomic,strong) NSMutableArray * dataArray;
@property (nonatomic,strong) NSMutableArray *pArr; //存储省份model
@property (nonatomic,strong) NSMutableArray *cArr;
@property (nonatomic,strong) NSMutableArray *tArr;
@property (nonatomic,strong) NSMutableArray *provinceArr;//省份名字
@property (nonatomic,strong) NSMutableArray *selectedArray;//记录选择
@property (nonatomic,strong) NSMutableDictionary *totalDict;

//最后获取到的省市县 model
@property (strong, nonatomic)ProvinceModel *pModel;
@property (strong, nonatomic)CityModel *cModel;
@property (strong, nonatomic)TownModel *tModel;
@end

@implementation CitiesDataTool

static CitiesDataTool *shareInstance = nil;

#pragma mark - Singleton
+ (CitiesDataTool *)sharedManager
{
    @synchronized (self) {
        if (shareInstance == nil) {
            shareInstance = [[self alloc] init];
        }
    }
    return shareInstance;
}

+ (instancetype)allocWithZone:(struct _NSZone *)zone
{
    @synchronized (self) {
        if (shareInstance == nil) {
            shareInstance = [super allocWithZone:zone];
        }
    }
    return shareInstance;
}

- (id)copy
{
    return shareInstance;
}

- (id)init
{
    self = [super init];
    if (self) {
        
    }
    return self;
}

//发送网络请求，获取省市区数据
- (void)requestGetData:(NSString *)locationStr{
   NSError *error;
    NSData *data = [locationStr dataUsingEncoding:NSUTF8StringEncoding];
    NSArray * jsonObjectArr =[NSJSONSerialization JSONObjectWithData:data
                                                  options:0
                                                    error:&error];
    NSLog(@"%@",jsonObjectArr);
  
    self.totalDict = [[NSMutableDictionary alloc] init];
   // [self.totalDict removeAllObjects];
    for (NSDictionary *dict in jsonObjectArr) {
            ProvinceModel *model = [[ProvinceModel alloc] init];
            [model setValuesForKeysWithDictionary:dict];
            NSMutableArray *arr =  [self.totalDict objectForKey:model.parentId];
        
        if (!arr) {
            arr = [NSMutableArray new];
        }
            [arr addObject:model];
            [self.totalDict setValue:arr forKey:model.parentId];
   }
}

-(NSArray *)QqueryAllRecordWithProviceID:(NSNumber *)ID{
    NSLog(@"省份:%@",[self.totalDict objectForKey:ID]);
   return [self.totalDict objectForKey:ID];
}
-(NSArray *)QqueryAllRecordWithCityID:(NSNumber *)ID{
    return [self.totalDict objectForKey:ID];
}
-(NSArray *)QqueryAllRecordWithTownID:(NSNumber *)ID{
    return [self.totalDict objectForKey:ID];
}
-(NSArray *)QqueryAllRecordWithStreetID:(NSNumber *)ID{
    return [self.totalDict objectForKey:ID];
}

@end


