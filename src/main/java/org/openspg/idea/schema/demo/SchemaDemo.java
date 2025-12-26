package org.openspg.idea.schema.demo;

public class SchemaDemo {

    public static String getHighlighterText() {
        return """
                namespace SampleDoc
                
                # 定义文本块
                Chunk(文本块): EntityType
                    properties:
                        content(内容): Text
                            index: TextAndVector
                
                HumanBodyPart(人体部位): ConceptType
                    hypernymPredicate: isA
                
                Symptom(症状): EntityType
                     properties:
                        desc(描述): Text
                            index: Text
                        semanticType(语义类型): Text
                            index: Text
                
                Indicator(医学指征): EntityType
                    properties:
                        desc(描述): Text
                            index: Text
                        semanticType(语义类型): Text
                            index: Text
                
                Disease(疾病): EntityType
                    properties:
                        desc(描述): Text
                            index: Text
                        semanticType(语义类型): Text
                            index: Text
                        complication(并发症): Disease
                            constraint: MultiValue
                        commonSymptom(常见症状): Symptom
                            constraint: MultiValue
                        diseaseSite(发病部位): HumanBodyPart
                            constraint: MultiValue
                    relations:
                        abnormal(异常指征): Indicator
                
                Others(其他): EntityType
                    properties:
                        desc(描述): Text
                            index: TextAndVector
                        semanticType(语义类型): Text
                            index: Text
                
                Space(场地): EntityType
                
                CableProtectionPipe(电缆保护管): EntityType
                
                SpecialLightingCable(专用照明电缆): EntityType
                
                Runway(跑道) -> Space:
                    relations:
                        contains(包含): CableProtectionPipe
                        contains(包含): SpecialLightingCable""";
    }

    public static String getCodeStyleText() {
        return """
                namespace SampleDoc
                
                # 定义文本块
                Chunk(文本块): EntityType
                    properties:
                        content(内容): Text
                            index: TextAndVector
                
                HumanBodyPart(人体部位): ConceptType
                    hypernymPredicate: isA
                
                Symptom(症状): EntityType
                     properties:
                        desc(描述): Text
                            index: Text
                        semanticType(语义类型): Text
                            index: Text
                
                Indicator(医学指征): EntityType
                    properties:
                        desc(描述): Text
                            index: Text
                        semanticType(语义类型): Text
                            index: Text
                
                Disease(疾病): EntityType
                    properties:
                        desc(描述): Text
                            index: Text
                        semanticType(语义类型): Text
                            index: Text
                        complication(并发症): Disease
                            constraint: MultiValue
                        commonSymptom(常见症状): Symptom
                            constraint: MultiValue
                        diseaseSite(发病部位): HumanBodyPart
                            constraint: MultiValue
                    relations:
                        abnormal(异常指征): Indicator
                
                Others(其他): EntityType
                    properties:
                        desc(描述): Text
                            index: TextAndVector
                        semanticType(语义类型): Text
                            index: Text
                
                Space(场地): EntityType
                
                CableProtectionPipe(电缆保护管): EntityType
                
                SpecialLightingCable(专用照明电缆): EntityType
                
                Runway(跑道) -> Space:
                    relations:
                        contains(包含): CableProtectionPipe
                        contains(包含): SpecialLightingCable""";
    }

}
