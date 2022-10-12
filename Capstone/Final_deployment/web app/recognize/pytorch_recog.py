#!/usr/bin/python
import torch
from torchvision import transforms
import torch.nn as nn
import torch.nn.functional as F
from PIL import Image


class Net(nn.Module):
    def __init__(self):
        super(Net, self).__init__()
        self.conv1 = nn.Conv2d(in_channels=3, out_channels=6, kernel_size=5)
        self.pool1 = nn.MaxPool2d(kernel_size=2, stride=2)
        self.conv2 = nn.Conv2d(in_channels=6, out_channels=16, kernel_size=5)
        self.fc1 = nn.Linear(in_features=16 * 72 * 197, out_features=120)
        self.fc2 = nn.Linear(in_features=120, out_features=84)
        self.fc3 = nn.Linear(in_features=84, out_features=10)

    def forward(self, x):
        # Max pooling over a (2, 2) window
        x = F.max_pool2d(F.relu(self.conv1(x)), (2, 2))
        # If the size is a square you can only specify a single number
        x = F.max_pool2d(F.relu(self.conv2(x)), 2)
        #         print(x.shape)
        # 特征图转换为一个１维的向量
        x = x.view(-1, self.num_flat_features(x))
        x = F.relu(self.fc1(x))
        x = F.relu(self.fc2(x))
        x = self.fc3(x)
        return x

    def num_flat_features(self, x):
        size = x.size()[1:]  # all dimensions except the batch dimension
        num_features = 1
        for s in size:
            num_features *= s
        return num_features


data_transform = transforms.Compose(
    [transforms.Resize((1000, 1000)),
     transforms.CenterCrop((300, 800)),
     transforms.ToTensor()])


def image_recog(file_path):
    net = Net()
    net = torch.load('./model/model_3.pth', map_location='cpu')

    # load a image
    image = Image.open(file_path)

    image_transformed = data_transform(image)
    print(image_transformed.size())

    Medicine_names = ['Advil', 'Demazin', 'Panadol']

    image_transformed = image_transformed.unsqueeze(0)
    output = net(image_transformed)
    predict_value, predict_idx = torch.max(output, 1)  # 求指定维度的最大值，返回最大值以及索引

    return Medicine_names[predict_idx]

if __name__ == "__main__":
    print(image_recog("/home/yang/Desktop/test_image/20190906_094004.jpg"))