import math

DisplayWidth_mm = 474
DisplayHeight_mm = 297
DisplayWidth_px = 1680
DisplayHeight_px = 1050
HorizontalOffset_mm = 0
VerticalOffset_mm = 36
DistanceDisplaySubject_mm = 550
VerticalAxisFromTopToBottom = True

def dva2pixel_X(x_angle):
    '''Convert X degrees of visual angle to X pixel coordinates'''

    CenterX_mm = float(DisplayWidth_mm) * 0.5 + float(HorizontalOffset_mm)

    InputX_mm = math.tan(x_angle * math.pi / 180) * DistanceDisplaySubject_mm + CenterX_mm

    x_px =  InputX_mm * float(DisplayWidth_px) / float(DisplayWidth_mm)

    return x_px

def dva2pixel_Y(y_angle):
    '''Convert Y degrees of visual angle to Y pixel coordinates'''

    CenterY_mm = float(DisplayHeight_mm) * 0.5 + float(VerticalOffset_mm)

    InputY_mm = math.tan(y_angle * math.pi / 180) * DistanceDisplaySubject_mm + CenterY_mm

    if VerticalAxisFromTopToBottom:
        y_px = DisplayHeight_px - InputY_mm * float(DisplayHeight_px) / float(DisplayHeight_mm)
    else:
        y_px = InputY_mm * float(DisplayHeight_px) / float(DisplayHeight_mm)

    return y_px

def pixel2dva(x_px, y_px):
    '''Convert pixel coordinates to degrees of visual angle'''

    # X degrees
    CenterX_mm = float(DisplayWidth_mm) * 0.5 + float(HorizontalOffset_mm)

    InputX_mm = float(x_px) * float(DisplayWidth_mm) / float(DisplayWidth_px)

    distanceX_mm = InputX_mm - CenterX_mm

    x_angle = math.atan(distanceX_mm / DistanceDisplaySubject_mm) * 180 / math.pi

    # Y degrees
    CenterY_mm = float(DisplayHeight_mm) * 0.5 + float(VerticalOffset_mm)

    if (VerticalAxisFromTopToBottom):
        InputY_mm = (DisplayHeight_px - float(y_px)) * float(DisplayHeight_mm) / float(DisplayHeight_px)
    else:
        InputY_mm = float(y_px) * float(DisplayHeight_mm) / float(DisplayHeight_px)

    distanceY_mm = InputY_mm - CenterY_mm

    y_angle = math.atan(distanceY_mm / DistanceDisplaySubject_mm) * 180 / math.pi

    return x_angle, y_angle