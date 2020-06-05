#import "RawsensorsPlugin.h"
#if __has_include(<rawsensors/rawsensors-Swift.h>)
#import <rawsensors/rawsensors-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "rawsensors-Swift.h"
#endif

@implementation RawsensorsPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftRawsensorsPlugin registerWithRegistrar:registrar];
}
@end
